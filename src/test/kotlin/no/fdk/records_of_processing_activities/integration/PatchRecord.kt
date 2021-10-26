package no.fdk.records_of_processing_activities.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.service.toDTO
import no.fdk.records_of_processing_activities.utils.*
import no.fdk.records_of_processing_activities.utils.jwk.Access
import no.fdk.records_of_processing_activities.utils.jwk.JwtToken
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals

private val mapper = jacksonObjectMapper().findAndRegisterModules()

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = ["spring.profiles.active=integration-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ApiTestContext.Initializer::class])
@Tag("integration")
class PatchRecord : ApiTestContext() {

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations/111222333/records/${RECORD_TO_BE_PATCHED.id}", HttpMethod.PATCH,
            body = mapper.writeValueAsString(RECORD_PATCH))

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong org access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/123/records/${RECORD_TO_BE_PATCHED.id}", HttpMethod.PATCH,
            token = JwtToken(Access.ORG_WRITE).toString(), body = mapper.writeValueAsString(RECORD_PATCH))

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Forbidden for wrong read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations/111222333/records/${RECORD_TO_BE_PATCHED.id}", HttpMethod.PATCH,
            token = JwtToken(Access.ORG_READ).toString(), body = mapper.writeValueAsString(RECORD_PATCH))

        assertEquals(HttpStatus.FORBIDDEN.value(), rsp["status"])
    }

    @Test
    fun `Ok - Created - for write access`() {
        val rspBefore = authorizedRequest(
            port, "/api/organizations/111222333/records/${RECORD_TO_BE_PATCHED.id}", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString())

        val bodyBefore: RecordDTO = mapper.readValue(rspBefore["body"] as String)
        assertEquals(RECORD_TO_BE_PATCHED.toDTO(), bodyBefore)

        val rsp = authorizedRequest(
            port, "/api/organizations/111222333/records/${RECORD_TO_BE_PATCHED.id}", HttpMethod.PATCH,
            token = JwtToken(Access.ORG_WRITE).toString(), body = mapper.writeValueAsString(RECORD_PATCH))
        assertEquals(HttpStatus.OK.value(), rsp["status"])

        val body: RecordDTO = mapper.readValue(rsp["body"] as String)

        val expected = RECORD_PATCH.copy(
            id = RECORD_TO_BE_PATCHED.id,
            organizationId = RECORD_TO_BE_PATCHED.organizationId,
            updatedAt = body.updatedAt
        )

        assertEquals(expected, body)
    }

}
