package com.pynguins.auth.types

data class Token(val username: String, val roles: List<String>)
