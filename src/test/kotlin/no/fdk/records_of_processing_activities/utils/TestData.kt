package no.fdk.records_of_processing_activities.utils

import com.google.common.collect.ImmutableMap
import no.fdk.records_of_processing_activities.model.*
import org.bson.types.ObjectId

const val LOCAL_SERVER_PORT = 5000
const val WIREMOCK_TEST_URI = "http://localhost:$LOCAL_SERVER_PORT"

const val MONGO_USER = "testuser"
const val MONGO_PASSWORD = "testpassword"
const val MONGO_PORT = 27017
const val MONGO_DB_NAME = "records-of-processing-activities"

val MONGO_ENV_VALUES: Map<String, String> = ImmutableMap.of(
    "MONGO_INITDB_ROOT_USERNAME", MONGO_USER,
    "MONGO_INITDB_ROOT_PASSWORD", MONGO_PASSWORD
)

val RECORD_DBO_0 = RecordDBO(
    id = ObjectId(),
    recordId = "record0",
    status = RecordStatus.APPROVED,
    organizationId = "123456789",
    null, null, null, null, null,
    null, null, null, null,
    null, null, null, null,
    null, null, null, null, null,
)

val RECORD_DTO_0 = RecordDTO(
    id = "record0",
    status = RecordStatus.APPROVED,
    organizationId = "123456789",
    null, null, null, null, null,
    null, null, null, null,
    null, null, null, null,
    null, null, null, null, null,
)

val RECORD_DBO_1 = RecordDBO(
    id = ObjectId(),
    recordId = "record1",
    status = RecordStatus.DRAFT,
    organizationId = "123456789",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_DTO_1 = RecordDTO(
    id = "record1",
    status = RecordStatus.DRAFT,
    organizationId = "123456789",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_DBO_2 = RecordDBO(
    id = ObjectId(),
    recordId = "record2",
    status = RecordStatus.DRAFT,
    organizationId = "987654321",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_DTO_2 = RecordDTO(
    id = "record2",
    status = RecordStatus.DRAFT,
    organizationId = "987654321",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_TO_BE_CREATED = RecordDTO(
    id = null, status = null, organizationId = null,
    null, title = "To be created", null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val ORGANIZATION_DBO_0 = OrganizationDBO(
    id = ObjectId(),
    organizationId = "123456789",
    null, null, null
)

val ORGANIZATION_DTO_0 = Organization(
    id = "123456789",
    null, null, null
)

fun recordDbPopulation() = listOf(RECORD_DBO_0, RECORD_DBO_1, RECORD_DBO_2)
    .map { it.mapDBO() }

fun orgDbPopulation() = listOf(ORGANIZATION_DBO_0).map { it.mapDBO() }

private fun RecordDBO.mapDBO(): org.bson.Document =
    org.bson.Document()
        .append("_id", id)
        .append("recordId", recordId)
        .append("organizationId", organizationId)
        .append("status", status.toString())

private fun OrganizationDBO.mapDBO(): org.bson.Document =
    org.bson.Document()
        .append("_id", id)
        .append("organizationId", organizationId)
