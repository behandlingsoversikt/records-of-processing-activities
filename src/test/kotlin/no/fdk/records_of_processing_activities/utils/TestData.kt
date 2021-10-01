package no.fdk.records_of_processing_activities.utils

import com.google.common.collect.ImmutableMap
import no.fdk.records_of_processing_activities.model.*
import org.bson.types.ObjectId
import java.time.LocalDate

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

val RECORD_TO_BE_DELETED = RecordDBO(
    id = ObjectId(),
    recordId = "tobedeleted",
    status = RecordStatus.DRAFT,
    organizationId = "111111111",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_TO_BE_PATCHED = RecordDBO(
    id = ObjectId(),
    recordId = "tobepatched",
    status = RecordStatus.DRAFT,
    organizationId = "111222333",
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null, null, null,
    null, null, null,
)

val RECORD_PATCH = RecordDTO(
    id = null, organizationId = null,
    status = RecordStatus.APPROVED,
    dataProcessorContactDetails = arrayListOf(
        ContactPoint(
            name = "Test Testbrukerg",
            email = "test@bruker.no",
            phone = "1337Testtelefon")),
    dataProcessingAgreements = arrayListOf(
        DataProcessingAgreement(
            dataProcessorName = "Behandler",
            agreementUrl = "Avtalen")),
    categories = arrayListOf(Categories(
        personalDataCategories = arrayListOf("Identifikasjonsopplysninger","Helseopplysninger"),
        dataSubjectCategories = "Ansatte")),
    commonDataControllerContact = CommonDataControllerContact(
        companies = "testvirksomhet",
        distributionOfResponsibilities = "Ansvarsfordeling",
        contactPoints = arrayListOf(ContactPoint(
            name = "kontaktpunkt",
            email = "kon@taktpun.kt",
            phone = "123459876qs"))),
    title = "Tittel",
    purpose = "Formål",
    articleSixBasis = arrayListOf(ArticleSix(legality = "6.1.e",referenceUrl = "https://legalreference.no")),
    otherArticles = OtherArticles(
        articleNine = Article(checked = false,referenceUrl = ""),
        articleTen = Article(checked = true, referenceUrl = "Annen lovgivning")),
    businessAreas = arrayListOf("område"),
    relatedDatasets = arrayListOf("1c941ffb-1849-44a6-ba06-ce6b1ddacc60"),
    securityMeasures = "Nulla vitae elit libero",
    plannedDeletion = "Planlagt sletting",
    dataProtectionImpactAssessment = DataProtectionImpactAssessment(
        conducted = true,
        assessmentReportUrl = "https://www.link.no"),
    personalDataSubjects = "Ansatt",
    privacyProcessingSystems = "https://www.link.com",
    recipientCategories = arrayListOf("Offentlige myndigheter","Hjelpeorganisasjoner"),
    dataTransfers = DataTransfers(
        transferred = true,
        thirdCountryRecipients = "Sør Afrika",
        guarantees = "Nelson Mandela"),
    updatedAt = LocalDate.of(2021, 8, 8)
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

val ORGANIZATION_TO_BE_CREATED = Organization(
    id = null,
    dataProtectionOfficer = ContactDetails(name = "Data Protector", email = "data@protection.com",
        phone = "11223344", address = "Offisergata 5"),
    dataControllerRepresentative = ContactDetails(name = "Data Controller", email = "data@controller.com",
        phone = "55667788", address = "Representantgata 5"),
    dataControllerRepresentativeInEU = ContactDetails(name = "Data EU Controller", email = "data@controller.eu",
        phone = "99887766", address = "Europagata 5")
)

val ORGANIZATION_TO_BE_PATCHED = OrganizationDBO(
    id = ObjectId(),
    organizationId = "111222333",
    dataProtectionOfficer = ContactDetails(name = "Data Protector", email = "data@protection.com",
        phone = "11223344", address = "Offisergata 5"),
    dataControllerRepresentative = null,
    dataControllerRepresentativeInEU = ContactDetails(name = "Data EU Controller", email = "data@controller.eu",
        phone = "99887766", address = "Europagata 5")
)

val ORGANIZATION_PATCH = Organization(
    id = null,
    dataControllerRepresentative = ContactDetails(name = "Data Controller", email = "data@controller.com",
        phone = "55667788", address = "Representantgata 5"), null, null
)

fun recordDbPopulation() = listOf(RECORD_DBO_0, RECORD_DBO_1, RECORD_DBO_2, RECORD_TO_BE_DELETED, RECORD_TO_BE_PATCHED)
    .map { it.mapDBO() }

fun orgDbPopulation() = listOf(ORGANIZATION_DBO_0, ORGANIZATION_TO_BE_PATCHED).map { it.mapDBO() }

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
        .append("dataControllerRepresentative", dataControllerRepresentative?.mapDBO())
        .append("dataControllerRepresentativeInEU", dataControllerRepresentativeInEU?.mapDBO())
        .append("dataProtectionOfficer", dataProtectionOfficer?.mapDBO())

private fun ContactDetails.mapDBO(): org.bson.Document =
    org.bson.Document()
        .append("name", name)
        .append("email", email)
        .append("phone", phone)
        .append("address", address)
