package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.Organization
import no.fdk.records_of_processing_activities.model.OrganizationDBO
import no.fdk.records_of_processing_activities.model.RecordDBO
import no.fdk.records_of_processing_activities.model.RecordDTO

fun OrganizationDBO.toDTO() =
    Organization(
        id = organizationId,
        dataControllerRepresentative,
        dataControllerRepresentativeInEU,
        dataProtectionOfficer
    )

fun RecordDBO.toDTO() =
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
