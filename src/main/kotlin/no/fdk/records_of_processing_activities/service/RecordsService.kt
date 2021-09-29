package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.Organization
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.repository.OrganizationRepository
import no.fdk.records_of_processing_activities.repository.RecordRepository
import org.springframework.stereotype.Service

@Service
class RecordsService(
    private val recordRepository: RecordRepository,
    private val organizationRepository: OrganizationRepository
) {

    fun getRecords(organizationId: String): List<RecordDTO> =
        recordRepository.findAllByOrganizationId(organizationId).map { it.toDTO() }

    fun getRecordById(recordId: String, organizationId: String): RecordDTO? =
        recordRepository.getByRecordIdAndOrganizationId(recordId, organizationId)?.toDTO()

    fun getRepresentatives(organizationId: String): Organization? =
        organizationRepository.getByOrganizationId(organizationId)?.toDTO()

}
