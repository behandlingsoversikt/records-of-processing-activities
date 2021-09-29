package no.fdk.records_of_processing_activities.repository

import no.fdk.records_of_processing_activities.model.RecordDBO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RecordRepository : MongoRepository<RecordDBO, String> {
    fun findAllByOrganizationId(organizationId: String): List<RecordDBO>
    fun getByRecordIdAndOrganizationId(recordId: String, organizationId: String): RecordDBO?
}
