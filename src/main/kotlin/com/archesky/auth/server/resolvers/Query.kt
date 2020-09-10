@file:Suppress("unused")

package com.archesky.auth.server.resolvers
import com.archesky.auth.server.service.TokenService
import com.archesky.auth.server.types.Role
import com.archesky.auth.server.types.Token
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.net.MalformedURLException
import java.net.URL

@Component
class Query(val tokenService: TokenService): GraphQLQueryResolver {
    private fun getHost(dataFetchingEnvironment: DataFetchingEnvironment): String {
        val defaultContext = dataFetchingEnvironment.getContext<DefaultGraphQLServletContext>()
        val request = defaultContext.httpServletRequest;
        try {
            return request.getHeader("hostname")
        } catch (e: IllegalStateException) {
            // Do nothing
        }
        try {
            return request.getHeader("Host")
        } catch (e: IllegalStateException) {
            // Do nothing
        }
        try {
            return URL(request.getHeader("Origin")).host
        } catch (e: IllegalStateException) {
            // Do nothing
        } catch (e: MalformedURLException) {
            // Do nothing
        }
        return "localhost"
    }

    @Suppress("UNCHECKED_CAST")
    fun checkToken(token: String, dataFetchingEnvironment: DataFetchingEnvironment): Token {
        val validatedToken = tokenService.validateToken(token, getHost(dataFetchingEnvironment))
        val roleList = ArrayList<Role>()
        val realmAccess = "realm_access"
        if (validatedToken.claims.containsKey(realmAccess)) {
            for (role in validatedToken.claims[realmAccess]!!.asMap()) {
                roleList.add(Role(realmAccess, (role.value as ArrayList<String>)))
            }
        }
        val resourceAccess = "resource_access"
        if (validatedToken.claims.containsKey(resourceAccess)) {
            for (role in validatedToken.claims[resourceAccess]!!.asMap()) {
                roleList.add(Role(role.key, (role.value as LinkedHashMap<String, ArrayList<String>>)["roles"]!!))
            }
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
