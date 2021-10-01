package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.*
import org.bson.types.ObjectId
import java.time.LocalDate
import java.util.*

fun OrganizationDBO.toDTO(): Organization =
    Organization(
        id = organizationId,
        dataControllerRepresentative,
        dataControllerRepresentativeInEU,
        dataProtectionOfficer
    )

fun OrganizationDBO.patchValues(newValues: Organization): OrganizationDBO =
    copy(
        dataControllerRepresentative = newValues.dataControllerRepresentative
            ?: dataControllerRepresentative,
        dataControllerRepresentativeInEU = newValues.dataControllerRepresentativeInEU
            ?: dataControllerRepresentativeInEU,
        dataProtectionOfficer = newValues.dataProtectionOfficer
            ?: dataProtectionOfficer
    )

fun Organization.mapForCreate(currentOrganizationId: String): OrganizationDBO =
    OrganizationDBO(
        id = ObjectId(),
        organizationId = currentOrganizationId,
        dataControllerRepresentative,
        dataControllerRepresentativeInEU,
        dataProtectionOfficer
    )

fun RecordDBO.toDTO(): RecordDTO =
    RecordDTO(
        id = recordId,
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
        updatedAt = patch.updatedAt
    )

fun RecordDTO.mapForCreate(currentOrganizationId: String): RecordDBO =
    RecordDBO(
        id = ObjectId(),
        recordId = UUID.randomUUID().toString(),
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
        updatedAt
    )
