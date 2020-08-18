package com.archesky.auth.server.service

import com.archesky.auth.server.ApplicationProperties
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import graphql.GraphQLException
import org.springframework.stereotype.Service
import java.lang.String.format
import java.net.URL
import java.security.interfaces.RSAPublicKey
import java.text.SimpleDateFormat
import java.util.*

@Service
class TokenService(val properties: ApplicationProperties) {
    fun validateToken(token: String, domain: String): DecodedJWT {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        val jwt = JWT.decode(token)
        val provider: JwkProvider = UrlJwkProvider(URL(format(properties.certificateURL, domain)))
        val jwk = provider[jwt.keyId]
        val algorithm: Algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
        algorithm.verify(jwt)
        if (jwt.issuer != format(properties.issuer, domain)) {
            throw GraphQLException(
                    "Token validation failed: " +
                            "Invalid issuer (Expected: '${format(properties.issuer, domain)}' but got '${jwt.issuer}')"
            )
        }
        if (jwt.expiresAt.before(Calendar.getInstance().time)) {
            throw GraphQLException(
                    "Token validation failed: " +
                            "The token expired at ${sdf.format(jwt.expiresAt)}"
            )
        }
        return jwt
    }
}
