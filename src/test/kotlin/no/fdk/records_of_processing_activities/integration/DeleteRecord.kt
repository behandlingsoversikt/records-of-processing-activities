package no.fdk.records_of_processing_activities.integration

import no.fdk.records_of_processing_activities.utils.jwk.Access
import no.fdk.records_of_processing_activities.utils.jwk.JwtToken
import no.fdk.records_of_processing_activities.utils.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = ["spring.profiles.active=integration-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ApiTestContext.Initializer::class])
@Tag("integration")
class DeleteRecord : ApiTestContext() {

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations/111111111/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.DELETE)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong org access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.DELETE,
            token = JwtToken(Access.ORG_WRITE).toString())

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Not found response when record is not in given organization`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/111111111/records/${RECORD_DBO_0.recordId}", HttpMethod.DELETE,
            token = JwtToken(Access.ORG_WRITE).toString())

        assertEquals(HttpStatus.NOT_FOUND.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/111111111/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.DELETE,
            token = JwtToken(Access.ORG_READ).toString())

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Deletes record for write access`() {
        val rspBefore = authorizedRequest(
            port, "/api/organizations/111111111/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())

        assertEquals(HttpStatus.OK.value(), rspBefore["status"])

        val rsp = authorizedRequest(
            port, "/api/organizations/111111111/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.DELETE,
            token = JwtToken(Access.ORG_WRITE).toString())

        assertEquals(HttpStatus.NO_CONTENT.value(), rsp["status"])

        val rspAfter = authorizedRequest(
            port, "/api/organizations/111111111/records/${RECORD_TO_BE_DELETED.recordId}", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())

        assertEquals(HttpStatus.NOT_FOUND.value(), rspAfter["status"])
    }

}
