package no.fdk.records_of_processing_activities.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.model.RecordStatus
import no.fdk.records_of_processing_activities.utils.ApiTestContext
import no.fdk.records_of_processing_activities.utils.RECORD_DTO_0
import no.fdk.records_of_processing_activities.utils.RECORD_TO_BE_CREATED
import no.fdk.records_of_processing_activities.utils.authorizedRequest
import no.fdk.records_of_processing_activities.utils.jwk.Access
import no.fdk.records_of_processing_activities.utils.jwk.JwtToken
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val mapper = jacksonObjectMapper()

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = ["spring.profiles.active=integration-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ApiTestContext.Initializer::class])
@Tag("integration")
class CreateRecord : ApiTestContext() {

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations/111111111/records", HttpMethod.POST,
            body = mapper.writeValueAsString(RECORD_TO_BE_CREATED))

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong org access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123/records", HttpMethod.POST,
            token = JwtToken(Access.ORG_WRITE).toString(), body = mapper.writeValueAsString(RECORD_TO_BE_CREATED))

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/111111111/records", HttpMethod.POST,
            token = JwtToken(Access.ORG_READ).toString(), body = mapper.writeValueAsString(RECORD_TO_BE_CREATED))

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Ok - Created - for write access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/111111111/records", HttpMethod.POST,
            token = JwtToken(Access.ORG_WRITE).toString(), body = mapper.writeValueAsString(RECORD_TO_BE_CREATED))
        assertEquals(HttpStatus.CREATED.value(), rsp["status"])
        val newId = (((rsp["header"] as Map<*, *>)["Location"] as List<*>)[0] as String).split("/api/organizations/111111111/records/")[1]

        val rspAfter = authorizedRequest(
            port, "/api/organizations/111111111/records/$newId", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())

        val expected = RECORD_TO_BE_CREATED.copy(id = newId, status = RecordStatus.DRAFT, organizationId = "111111111")

        val body: RecordDTO = mapper.readValue(rspAfter["body"] as String)

        assertEquals(expected, body)
    }

}
