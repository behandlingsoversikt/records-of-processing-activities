package no.fdk.records_of_processing_activities.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "records")
data class RecordDBO(
    @Id
    val id: String,
    val status: RecordStatus?,
    val organizationId: String?,
    val purpose: String?,
    val title: String?,
    val categories: ArrayList<Categories>?,
    val articleSixBasis: ArrayList<ArticleSix>?,
    val otherArticles: OtherArticles?,
    val businessAreas: ArrayList<String>?,
    val relatedDatasets: ArrayList<String>?,
    val dataProcessingAgreements: ArrayList<DataProcessingAgreement>?,
    val dataProcessorContactDetails: ArrayList<ContactPoint>?,
    val commonDataControllerContact: CommonDataControllerContact?,
    val personalDataSubjects: String?,
    val securityMeasures: String?,
    val plannedDeletion: String?,
    val dataProtectionImpactAssessment: DataProtectionImpactAssessment?,
    val privacyProcessingSystems: String?,
    val recipientCategories: ArrayList<String>?,
    val dataTransfers: DataTransfers?,
    val updatedAt: LocalDateTime?
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class RecordDTO(
    val id: String?,
    val status: RecordStatus?,
    val organizationId: String?,
    val purpose: String?,
    val title: String?,
    val categories: ArrayList<Categories>?,
    val articleSixBasis: ArrayList<ArticleSix>?,
    val otherArticles: OtherArticles?,
    val businessAreas: ArrayList<String>?,
    val relatedDatasets: ArrayList<String>?,
    val dataProcessingAgreements: ArrayList<DataProcessingAgreement>?,
    val dataProcessorContactDetails: ArrayList<ContactPoint>?,
    val commonDataControllerContact: CommonDataControllerContact?,
    val personalDataSubjects: String?,
    val securityMeasures: String?,
    val plannedDeletion: String?,
    val dataProtectionImpactAssessment: DataProtectionImpactAssessment?,
    val privacyProcessingSystems: String?,
    val recipientCategories: ArrayList<String>?,
    val dataTransfers: DataTransfers?,
    val updatedAt: LocalDateTime?
)

data class PagedRecords(
    val pageNumber: Int,
    val pagesTotal: Int,
    val size: Int,
    val hits: List<RecordDTO>
)

data class ContactPoint(
    val name: String?,
    val email: String?,
    val phone: String?
)

data class Categories(
    val dataSubjectCategories: String?,
    val personalDataCategories: ArrayList<String>?
)

data class Article(
    val checked: Boolean?,
    val referenceUrl: String?
)

data class ArticleSix(
    val legality: String?,
    val referenceUrl: String?
)

data class ArticleLegality(
    val legality: String?,
    val checked: Boolean?,
    val referenceUrl: String?
)

data class ArticleWithLegalities(
    val checked: Boolean?,
    val referenceUrl: String?,
    val legalities: ArrayList<ArticleLegality>?
)

data class OtherArticles(
    val articleNine: ArticleWithLegalities?,
    val articleTen: Article?
)

data class DataProcessingAgreement(
    val dataProcessorName: String?,
    val agreementUrl: String?
)

data class CommonDataControllerContact(
    val commonDataControllerChecked: Boolean?,
    val companies: String?,
    val distributionOfResponsibilities: String?,
    val contactPoints: ArrayList<ContactPoint>?
)

data class DataProtectionImpactAssessment(
    val conducted: Boolean?,
    val assessmentReportUrl: String?
)

data class DataTransfers(
    val transferred: Boolean?,
    val thirdCountryRecipients: String?,
    val guarantees: String?,
)

enum class RecordStatus {
    DRAFT, APPROVED
}
