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
class GetRecordById : ApiTestContext() {

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations/123456789/records/${RECORD_DTO_0.id}", HttpMethod.GET)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong org access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123/records/${RECORD_DTO_0.id}", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString())

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Not found response when record is not in given organization`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records/${RECORD_DTO_2.id}", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString())

        assertEquals(HttpStatus.NOT_FOUND.value(), rsp["status"])
    }

    @Test
    fun `Returns record for read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records/${RECORD_DTO_0.id}", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString())
        val body: RecordDTO = mapper.readValue(rsp["body"] as String)

        assertEquals(RECORD_DTO_0 , body)
    }

    @Test
    fun `Returns record for write access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records/${RECORD_DTO_0.id}", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())
        val body: RecordDTO = mapper.readValue(rsp["body"] as String)

        assertEquals(RECORD_DTO_0 , body)
    }

    @Test
    fun `Returns record for root access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123456789/records/${RECORD_DTO_0.id}", HttpMethod.GET,
            token = JwtToken(Access.ROOT).toString())
        val body: RecordDTO = mapper.readValue(rsp["body"] as String)

        assertEquals(RECORD_DTO_0 , body)
    }

}
