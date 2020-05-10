package com.archesky.auth.server.types

data class Token(
        val username: String,
        val firstName: String?,
        val lastName: String?,
        val fullName: String?,
        val email: String?,
        val roles: List<Role>
)
