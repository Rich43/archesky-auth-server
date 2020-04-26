package com.pynguins.auth.server

import graphql.GraphQLError
import graphql.kickstart.execution.error.GraphQLErrorHandler
import org.springframework.stereotype.Component
import java.util.Collections.emptyList

@Component
class ErrorHandler: GraphQLErrorHandler {
    override fun processErrors(errors: MutableList<GraphQLError>?): MutableList<GraphQLError> {
        return errors ?: emptyList()
    }
}
