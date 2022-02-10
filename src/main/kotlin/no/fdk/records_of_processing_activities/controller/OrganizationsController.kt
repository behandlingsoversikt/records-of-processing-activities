package no.fdk.records_of_processing_activities.controller

import no.fdk.records_of_processing_activities.model.RecordCount
import no.fdk.records_of_processing_activities.service.EndpointPermissions
import no.fdk.records_of_processing_activities.service.RecordsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping(value = ["/api/organizations"], produces = ["application/json"])
class OrganizationsController(
    private val permissions: EndpointPermissions,
    private val service: RecordsService
) {

    @GetMapping
    fun getRecordCounts(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<List<RecordCount>> {
        val recordCounts = if(permissions.hasSysAdminPermission(jwt)) service.allRecordCounts()
        else service.recordCountForOrganizations(permissions.permittedOrganizations(jwt))

        return ResponseEntity(recordCounts, HttpStatus.OK)
    }

}
