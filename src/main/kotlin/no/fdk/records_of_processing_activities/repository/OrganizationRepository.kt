package no.fdk.records_of_processing_activities.repository

import no.fdk.records_of_processing_activities.model.RepresentativesDBO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : MongoRepository<RepresentativesDBO, String>
