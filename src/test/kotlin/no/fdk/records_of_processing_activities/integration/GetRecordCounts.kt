package no.fdk.records_of_processing_activities.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.fdk.records_of_processing_activities.model.PagedRecords
import no.fdk.records_of_processing_activities.model.RecordCount
import no.fdk.records_of_processing_activities.utils.*
import no.fdk.records_of_processing_activities.utils.jwk.Access
import no.fdk.records_of_processing_activities.utils.jwk.JwtToken
import org.junit.jupiter.api.BeforeAll
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
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [ApiTestContext.Initializer::class])
@Tag("integration")
class GetRecordCounts : ApiTestContext() {

    @BeforeAll
    fun resetDatabase() {
        resetDB()
    }

    @Test
    fun `Unauthorized when not logged in`() {
        val rsp = authorizedRequest(port, "/api/organizations", HttpMethod.GET)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), rsp["status"])
    }

    @Test
    fun `Returns record count for all organizations with root access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations", HttpMethod.GET,
            token = JwtToken(Access.ROOT).toString()
        )
        val body: List<RecordCount> = mapper.readValue(rsp["body"] as String)

        val expected: List<RecordCount> = listOf(
            RecordCount(organizationId = "123456789", recordCount = 2),
            RecordCount(organizationId = "987654321", recordCount = 1),
            RecordCount(organizationId = "111111111", recordCount = 1),
            RecordCount(organizationId = "111222333", recordCount = 1)
        )

        assertEquals(expected, body)
    }

    @Test
    fun `Returns record count for permitted organizations with write access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations", HttpMethod.GET,
            token = JwtToken(Access.ORG_WRITE).toString()
        )
        val body: List<RecordCount> = mapper.readValue(rsp["body"] as String)

        val expected: List<RecordCount> = listOf(
            RecordCount(organizationId = "123456789", recordCount = 2),
            RecordCount(organizationId = "111111111", recordCount = 1),
            RecordCount(organizationId = "111222333", recordCount = 1)
        )

        assertEquals(expected, body)
    }

    @Test
    fun `Returns record count for permitted organizations with read access`() {
        val rsp = authorizedRequest(
            port, "/api/organizations", HttpMethod.GET,
            token = JwtToken(Access.ORG_READ).toString()
        )
        val body: List<RecordCount> = mapper.readValue(rsp["body"] as String)

        val expected: List<RecordCount> = listOf(
            RecordCount(organizationId = "123456789", recordCount = 2),
            RecordCount(organizationId = "111111111", recordCount = 1),
            RecordCount(organizationId = "111222333", recordCount = 1)
        )

        assertEquals(expected, body)
    }

}