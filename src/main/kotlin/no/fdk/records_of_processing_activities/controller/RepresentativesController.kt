package no.fdk.records_of_processing_activities.controller

import no.fdk.records_of_processing_activities.model.Organization
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
@RequestMapping(value = ["/api/organizations/{organizationId}/representatives"], produces = ["application/json"])
class RepresentativesController(
    private val permissions: EndpointPermissions,
    private val service: RecordsService
) {

    @GetMapping
    fun getRepresentatives(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String
    ): ResponseEntity<Organization> {
        val representatives = service.getRepresentatives(organizationId)
        return when {
            !permissions.hasOrgReadPermission(jwt, organizationId) -> ResponseEntity(HttpStatus.FORBIDDEN)
            representatives == null -> ResponseEntity(HttpStatus.NOT_FOUND)
            else -> ResponseEntity(representatives, HttpStatus.OK)
        }
    }

    @PostMapping
    fun createRecord(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable organizationId: String,
        @RequestBody representatives: Organization
    ): ResponseEntity<Unit> =
        if (permissions.hasOrgWritePermission(jwt, organizationId)) {
            logger.info("creating representatives for $organizationId")
            service.createRepresentatives(representatives, organizationId).organizationId
                .let{
                    ResponseEntity(
                        locationHeaderForCreated(location = "/api/organizations/$it/representatives"),
                        HttpStatus.CREATED
                    )
                }
        } else ResponseEntity(HttpStatus.FORBIDDEN)

}
