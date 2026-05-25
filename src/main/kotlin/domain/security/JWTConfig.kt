package com.example.domain.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JWTConfig {
    private const val SECRET = "trackly_super_secret_key"

    private const val ISSUER = "trackly_server"

    private const val AUDIENCE = "trackly_users"

    private const val EXPIRE = 365L * 24 * 60 * 60 * 1000

    fun generateToken(userId: Long): String {
        return JWT
            .create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRE))
            .sign(Algorithm.HMAC256(SECRET))
    }
}