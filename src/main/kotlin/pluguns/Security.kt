package com.example.pluguns

import io.ktor.server.application.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val jwtAudience = "trackly_users"
    val jwtDomain = "trackly_server"
    val jwtRealm = "Trackly"
    val jwtSecret = "trackly_super_secret_key"

    authentication {
        jwt {
            realm = jwtRealm

            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("userId").asLong() != null) {
                    JWTPrincipal(credential.payload)
                }
                else null
            }
        }
    }
}