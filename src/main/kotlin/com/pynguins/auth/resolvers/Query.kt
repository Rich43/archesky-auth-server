package com.pynguins.auth.resolvers
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.pynguins.auth.types.Token
import org.springframework.stereotype.Component
import java.util.Collections.singletonList

@Component
class Query: GraphQLQueryResolver {
    fun checkToken(token: String): Token? {
        return Token("foo", singletonList("admin"))
    }
}
