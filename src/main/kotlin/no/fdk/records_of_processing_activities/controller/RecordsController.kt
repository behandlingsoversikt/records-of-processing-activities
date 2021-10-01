package no.fdk.records_of_processing_activities.controller

import no.fdk.records_of_processing_activities.model.PagedRecords
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.service.EndpointPermissions
import no.fdk.records_of_processing_activities.service.RecordsService
import no.fdk.records_of_processing_activities.utils.locationHeaderForCreated
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

private val logger = LoggerFactory.getLogger(RecordsController::class.java)

@RestController
@CrossOrigin
@RequestMapping(value = ["/api/organizations/{organizationId}/records"], produces = ["application/json"])
class RecordsController(
    private val permissions: EndpointPermissions,
    private val service: RecordsService
) {

    @GetMapping
    fun getRecords(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @RequestParam(value = "limit", required = false) limit: Int?,
        @RequestParam(value = "page", required = false) page: Int?
    ): ResponseEntity<PagedRecords> =
        if (permissions.hasOrgReadPermission(jwt, organizationId)) {
            ResponseEntity(service.getRecords(organizationId, limit ?: 100, page ?: 0), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.FORBIDDEN)

    @GetMapping(path = ["/{recordId}"])
    fun getRecordById(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @PathVariable recordId: String
    ): ResponseEntity<RecordDTO> {
        val record = service.getRecordById(recordId, organizationId)
        return when {
            !permissions.hasOrgReadPermission(jwt, organizationId) -> ResponseEntity(HttpStatus.FORBIDDEN)
            record == null -> ResponseEntity(HttpStatus.NOT_FOUND)
            else -> ResponseEntity(record, HttpStatus.OK)
        }
    }

    @PostMapping
    fun createRecord(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @RequestBody record: RecordDTO
    ): ResponseEntity<Unit> =
        if (permissions.hasOrgWritePermission(jwt, organizationId)) {
            logger.info("creating record for $organizationId")
            service.createRecord(record, organizationId).recordId
                .let{
                    ResponseEntity(
                        locationHeaderForCreated(location = "/api/organizations/$organizationId/records/$it"),
                        HttpStatus.CREATED
                    ) }
        } else ResponseEntity(HttpStatus.FORBIDDEN)

    @PatchMapping(path = ["/{recordId}"])
    fun patchRecord(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @PathVariable recordId: String,
        @RequestBody record: RecordDTO
    ): ResponseEntity<RecordDTO> =
        if (permissions.hasOrgWritePermission(jwt, organizationId)) {
            service.patchRecord(recordId, organizationId, record)
                ?.let{ ResponseEntity(it, HttpStatus.CREATED) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(HttpStatus.FORBIDDEN)

    @DeleteMapping(path = ["/{recordId}"])
    fun deleteRecord(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @PathVariable recordId: String
    ): ResponseEntity<Unit> =
        if (permissions.hasOrgWritePermission(jwt, organizationId)) {
            logger.info("deleting record $recordId for $organizationId")
            service.deleteRecord(recordId, organizationId)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else ResponseEntity(HttpStatus.FORBIDDEN)

}
