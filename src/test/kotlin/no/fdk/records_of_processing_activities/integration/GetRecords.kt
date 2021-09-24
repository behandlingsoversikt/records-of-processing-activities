package no.fdk.records_of_processing_activities.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fdk.records_of_processing_activities.utils.jwk.Access
import no.fdk.records_of_processing_activities.utils.jwk.JwtToken
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.utils.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

private val mapper = jacksonObjectMapper()

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = ["spring.profiles.active=integration-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ApiTestContext.Initializer::class])
@Tag("integration")
class GetRecords : ApiTestContext() {

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations/123456789/records", HttpMethod.GET)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong org access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123/records", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString())

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Returns all organization records for read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString())
        val body: List<RecordDTO> = mapper.readValue(rsp["body"] as String)

        assertEquals(listOf(RECORD_DTO_0, RECORD_DTO_1) , body)
    }

    @Test
    fun `Returns all organization records for write access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())
        val body: List<RecordDTO> = mapper.readValue(rsp["body"] as String)

        assertEquals(listOf(RECORD_DTO_0, RECORD_DTO_1) , body)
    }

    @Test
    fun `Returns all organization records for root access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records", HttpMethod.GET,
            token = JwtToken(Access.ROOT).toString())
        val body: List<RecordDTO> = mapper.readValue(rsp["body"] as String)

        assertEquals(listOf(RECORD_DTO_0, RECORD_DTO_1) , body)
    }

}
