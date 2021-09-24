package no.fdk.records_of_processing_activities.integration

import no.fdk.records_of_processing_activities.utils.ApiTestContext
import no.fdk.records_of_processing_activities.utils.apiGet
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = ["spring.profiles.active=integration-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
class LivenessIntegration : ApiTestContext() {

    @Test
    fun ping() {
        val response = apiGet(port, "/ping", null)
        assertEquals(HttpStatus.OK.value(), response["status"])
    }

    @Test
    fun ready() {
        val response = apiGet(port, "/ready", null)
        assertEquals(HttpStatus.OK.value(), response["status"])
    }

}
