package com.example.pluguns

import com.example.data.repository.UserRepositoryImpl
import com.example.domain.service.AuthService
import com.example.routes.authRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val userRepository = UserRepositoryImpl()
    val authService = AuthService(userRepository)

    routing {
        authRoutes(authService)

        authenticate {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("userId")?.asLong()

                if (userId == null) {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        "Invalid token"
                    )
                    return@get
                }

                println(principal?.payload?.claims)

                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "userId" to userId
                    )
                )
            }
        }
    }
}