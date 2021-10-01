package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.*
import no.fdk.records_of_processing_activities.repository.OrganizationRepository
import no.fdk.records_of_processing_activities.repository.RecordRepository
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class RecordsService(
    private val recordRepository: RecordRepository,
    private val organizationRepository: OrganizationRepository
) {

    fun getRecords(organizationId: String, limit: Int, page: Int): PagedRecords =
        recordRepository.findAllByOrganizationId(
            organizationId,
            Pageable.ofSize(limit).withPage(page)
        ).let {
            PagedRecords(
                pageNumber = it.number,
                pagesTotal = it.totalPages,
                size = it.size,
                hits = it.content.map { dbo -> dbo.toDTO() })
        }

    fun getRecordById(recordId: String, organizationId: String): RecordDTO? =
        recordRepository.getByRecordIdAndOrganizationId(recordId, organizationId)?.toDTO()

    fun deleteRecord(recordId: String, organizationId: String): Unit =
        recordRepository.getByRecordIdAndOrganizationId(recordId, organizationId)
            ?.run { recordRepository.delete(this) }
            ?: run { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

    fun createRecord(record: RecordDTO, organizationId: String): RecordDBO =
        recordRepository.save(record.mapForCreate(organizationId))

    fun patchRecord(recordId: String, organizationId: String, patch: RecordDTO): RecordDTO? =
        recordRepository.getByRecordIdAndOrganizationId(recordId, organizationId)
            ?.let { recordRepository.save(it.patchValues(patch)) }
            ?.toDTO()

    fun getRepresentatives(organizationId: String): Organization? =
        organizationRepository.getByOrganizationId(organizationId)?.toDTO()

    fun createRepresentatives(representatives: Organization, organizationId: String): OrganizationDBO =
        organizationRepository.save(representatives.mapForCreate(organizationId))

    fun patchRepresentatives(representatives: Organization, organizationId: String): Organization? =
        organizationRepository.getByOrganizationId(organizationId)
            ?.patchValues(representatives)
            ?.let{ organizationRepository.save(it) }
            ?.toDTO()

}
