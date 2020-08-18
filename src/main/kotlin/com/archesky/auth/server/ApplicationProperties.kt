package com.archesky.auth.server

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
data class ApplicationProperties (
        @Value("\${openid.connect.certificate.url}") val certificateURL: String,
        @Value("\${openid.connect.issuer}") val issuer: String,
        @Value("#{T(java.lang.Integer).parseInt(\${server.http.port})}") val httpPort: Int,
        @Value("#{T(java.lang.Integer).parseInt(\${server.port})}") val httpsPort: Int
)
