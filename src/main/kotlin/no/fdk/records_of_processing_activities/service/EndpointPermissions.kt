package no.fdk.records_of_processing_activities.service

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

private const val ROLE_ROOT_ADMIN = "system:root:admin"
private fun roleOrgAdmin(orgnr: String) = "organization:$orgnr:admin"
private fun roleOrgWrite(orgnr: String) = "organization:$orgnr:write"
private fun roleOrgRead(orgnr: String) = "organization:$orgnr:read"

@Component
class EndpointPermissions {

    fun hasOrgReadPermission(jwt: Jwt, organizationId: String?): Boolean {
        val authorities: String? = jwt.claims["authorities"] as? String
        return when {
            organizationId == null -> false
            authorities == null -> false
            hasOrgWritePermission(jwt, organizationId) -> true
            authorities.contains(roleOrgRead(organizationId)) -> true
            else -> false
        }
    }

    fun hasOrgWritePermission(jwt: Jwt, organizationId: String?): Boolean {
        val authorities: String? = jwt.claims["authorities"] as? String
        return when {
            organizationId == null -> false
            authorities == null -> false
            hasSysAdminPermission(jwt) -> true
            authorities.contains(roleOrgAdmin(organizationId)) -> true
            authorities.contains(roleOrgWrite(organizationId)) -> true
            else -> false
        }
    }

    private fun hasSysAdminPermission(jwt: Jwt): Boolean {
        val authorities: String? = jwt.claims["authorities"] as? String

        return authorities?.contains(ROLE_ROOT_ADMIN) ?: false
    }

}
