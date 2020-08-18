@file:Suppress("unused")

package com.archesky.auth.server.resolvers
import com.archesky.auth.server.service.TokenService
import com.archesky.auth.server.types.Role
import com.archesky.auth.server.types.Token
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class Query(val tokenService: TokenService): GraphQLQueryResolver {
    @Suppress("UNCHECKED_CAST")
    fun checkToken(token: String): Token {
        val validatedToken = tokenService.validateToken(token, "localhost")
        val roleList = ArrayList<Role>()
        for (role in validatedToken.claims["realm_access"]!!.asMap()) {
            roleList.add(Role("realm_access", (role.value as ArrayList<String>)))
        }
        for (role in validatedToken.claims["resource_access"]!!.asMap()) {
            roleList.add(Role(role.key, (role.value as LinkedHashMap<String, ArrayList<String>>)["roles"]!!))
        }
        return Token(
                validatedToken.getClaim("preferred_username").asString(),
                validatedToken.getClaim("given_name").asString(),
                validatedToken.getClaim("family_name").asString(),
                validatedToken.getClaim("name").asString(),
                validatedToken.getClaim("email").asString(),
                roleList
        )
    }
}
