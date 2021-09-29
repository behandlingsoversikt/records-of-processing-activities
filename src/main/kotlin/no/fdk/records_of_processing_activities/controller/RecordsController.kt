package no.fdk.records_of_processing_activities.controller

import no.fdk.records_of_processing_activities.model.PagedRecords
import no.fdk.records_of_processing_activities.model.RecordDTO
import no.fdk.records_of_processing_activities.service.EndpointPermissions
import no.fdk.records_of_processing_activities.service.RecordsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

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

}
