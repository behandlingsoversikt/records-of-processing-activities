package no.fdk.records_of_processing_activities.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

private const val ROLE_ROOT_ADMIN = "system:root:admin"
private fun roleOrgAdmin(orgnr: String) = "organization:$orgnr:admin"
private fun roleOrgWrite(orgnr: String) = "organization:$orgnr:write"
private fun roleOrgRead(orgnr: String) = "organization:$orgnr:read"

@Component
class EndpointPermissions(@Value("\${application.security.allowedOrgs}") private val allowedOrgs: Array<String>) {

    fun permittedOrganizations(jwt: Jwt): Set<String> {
        val authorities: String? = jwt.claims["authorities"] as? String
        val regex = Regex("""[0-9]{9}""")

        return authorities
            ?.let { regex.findAll(it)}
            ?.map { matchResult -> matchResult.value
                .replace(Regex("[A-Za-z:]"), "")}
            ?.toSet()
            ?: emptySet()
    }

    fun hasOrgReadPermission(jwt: Jwt, organizationId: String?): Boolean {
        val authorities: String? = jwt.claims["authorities"] as? String
        return when {
            organizationId == null -> false
            authorities == null -> false
            hasSysAdminPermission(jwt) -> true
            !allowedOrgs.contains(organizationId) -> false
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
            !allowedOrgs.contains(organizationId) -> false
            authorities.contains(roleOrgAdmin(organizationId)) -> true
            authorities.contains(roleOrgWrite(organizationId)) -> true
            else -> false
        }
    }

    fun hasSysAdminPermission(jwt: Jwt): Boolean {
        val authorities: String? = jwt.claims["authorities"] as? String

        return authorities?.contains(ROLE_ROOT_ADMIN) ?: false
    }

}
