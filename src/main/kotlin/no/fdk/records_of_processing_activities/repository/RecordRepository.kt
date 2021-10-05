package no.fdk.records_of_processing_activities.repository

import no.fdk.records_of_processing_activities.model.RecordDBO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RecordRepository : MongoRepository<RecordDBO, String> {
    fun findAllByOrganizationId(organizationId: String, pageable: Pageable): Page<RecordDBO>
    fun getByIdAndOrganizationId(id: String, organizationId: String): RecordDBO?
}
