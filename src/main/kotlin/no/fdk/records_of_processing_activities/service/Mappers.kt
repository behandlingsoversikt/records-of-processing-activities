package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.*
import java.time.LocalDateTime
import java.util.*

fun RepresentativesDBO.toDTO(): RepresentativesDTO =
    RepresentativesDTO(
        id,
        dataControllerRepresentative,
        dataControllerRepresentativeInEU,
        dataProtectionOfficer
    )

fun RepresentativesDBO.patchValues(newValues: RepresentativesDTO): RepresentativesDBO =
    copy(
        dataControllerRepresentative = newValues.dataControllerRepresentative
            ?: dataControllerRepresentative,
        dataControllerRepresentativeInEU = newValues.dataControllerRepresentativeInEU
            ?: dataControllerRepresentativeInEU,
        dataProtectionOfficer = newValues.dataProtectionOfficer
            ?: dataProtectionOfficer
    )

fun RepresentativesDTO.mapForCreate(organizationId: String): RepresentativesDBO =
    RepresentativesDBO(
        id = organizationId,
        dataControllerRepresentative,
        dataControllerRepresentativeInEU,
        dataProtectionOfficer
    )

fun RecordDBO.toDTO(): RecordDTO =
    RecordDTO(
        id,
        status,
        organizationId,
        purpose,
        title,
        categories,
        articleSixBasis,
        otherArticles,
        businessAreas,
        relatedDatasets,
        dataProcessingAgreements,
        dataProcessorContactDetails,
        commonDataControllerContact,
        personalDataSubjects,
        securityMeasures,
        plannedDeletion,
        dataProtectionImpactAssessment,
        privacyProcessingSystems,
        recipientCategories,
        dataTransfers,
        updatedAt
    )

fun RecordDBO.patchValues(patch: RecordDTO): RecordDBO =
    copy(
        status = patch.status,
        purpose = patch.purpose,
        title = patch.title,
        categories = patch.categories,
        articleSixBasis = patch.articleSixBasis,
        otherArticles = patch.otherArticles,
        businessAreas = patch.businessAreas,
        relatedDatasets = patch.relatedDatasets,
        dataProcessingAgreements = patch.dataProcessingAgreements,
        dataProcessorContactDetails = patch.dataProcessorContactDetails,
        commonDataControllerContact = patch.commonDataControllerContact,
        personalDataSubjects = patch.personalDataSubjects,
        securityMeasures = patch.securityMeasures,
        plannedDeletion = patch.plannedDeletion,
        dataProtectionImpactAssessment = patch.dataProtectionImpactAssessment,
        privacyProcessingSystems = patch.privacyProcessingSystems,
        recipientCategories = patch.recipientCategories,
        dataTransfers = patch.dataTransfers,
        updatedAt = LocalDateTime.now()
    )

fun RecordDTO.mapForCreate(currentOrganizationId: String): RecordDBO =
    RecordDBO(
        id = UUID.randomUUID().toString(),
        status = RecordStatus.DRAFT,
        organizationId = currentOrganizationId,
        purpose,
        title,
        categories,
        articleSixBasis,
        otherArticles,
        businessAreas,
        relatedDatasets,
        dataProcessingAgreements,
        dataProcessorContactDetails,
        commonDataControllerContact,
        personalDataSubjects,
        securityMeasures,
        plannedDeletion,
        dataProtectionImpactAssessment,
        privacyProcessingSystems,
        recipientCategories,
        dataTransfers,
        updatedAt = LocalDateTime.now()
    )

fun countOrganizationRecords(records: List<RecordDBO>): List<RecordCount> {
    val counts: MutableMap<String, Int> = mutableMapOf()
    records.forEach{ it.organizationId?.run {
        counts[this] = counts.getOrDefault(this, 0) + 1 }
    }

    return counts.map { RecordCount(organizationId = it.key, recordCount = it.value) }
}
