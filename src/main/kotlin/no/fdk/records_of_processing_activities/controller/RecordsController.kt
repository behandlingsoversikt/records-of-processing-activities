package no.fdk.records_of_processing_activities.controller

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
        @PathVariable organizationId: String
    ): ResponseEntity<List<RecordDTO>> =
        if (permissions.hasOrgReadPermission(jwt, organizationId)) {
            ResponseEntity(service.getRecords(organizationId), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.FORBIDDEN)

}
