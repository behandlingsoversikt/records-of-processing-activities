package no.fdk.records_of_processing_activities.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "representatives")
data class RepresentativesDBO(
    @Id
    val id: String,
    val dataControllerRepresentative: ContactDetails?,
    val dataControllerRepresentativeInEU: ContactDetails?,
    val dataProtectionOfficer: ContactDetails?
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class RepresentativesDTO(
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
