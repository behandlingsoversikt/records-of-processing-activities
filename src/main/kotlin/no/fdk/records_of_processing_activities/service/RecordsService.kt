package no.fdk.records_of_processing_activities.service

import no.fdk.records_of_processing_activities.model.Organization
import no.fdk.records_of_processing_activities.model.PagedRecords
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.repository.OrganizationRepository
import no.fdk.records_of_processing_activities.repository.RecordRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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

    fun getRepresentatives(organizationId: String): Organization? =
        organizationRepository.getByOrganizationId(organizationId)?.toDTO()

}
