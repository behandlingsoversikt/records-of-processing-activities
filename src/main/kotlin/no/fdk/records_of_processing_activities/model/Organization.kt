package no.fdk.records_of_processing_activities.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "organizations")
data class OrganizationDBO(
    @Id
    val id: ObjectId,
    val organizationId: String,
    val dataControllerRepresentative: ContactDetails?,
    val dataControllerRepresentativeInEU: ContactDetails?,
    val dataProtectionOfficer: ContactDetails?
)

data class Organization(
    val id: String?,
    val dataControllerRepresentative: ContactDetails?,
    val dataControllerRepresentativeInEU: ContactDetails?,
    val dataProtectionOfficer: ContactDetails?
)

data class ContactDetails (
    val name: String?,
    val email: String?,
    val phone: String?,
    val address: String?
)
