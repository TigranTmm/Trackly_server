package com.example.routes

import com.example.domain.service.AuthService
import com.example.dto.LoginRequest
import com.example.dto.LoginResponse
import com.example.dto.RegisterRequest
import com.example.dto.RequestResponce
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoutes(
    authService: AuthService
) {
    post("/register") {
        try {
            val request = call.receive<RegisterRequest>()

            val user = authService.register(
                email = request.email,
                password = request.password
            )

            call.respond(
                RequestResponce(
                    id = user.id,
                    email = user.email
                )
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: ""
            )
        }
    }

    post("/login") {
        try {
            val request = call.receive<LoginRequest>()

            val token = authService.login(
                request.email,
                request.password
            )

            call.respond(
                LoginResponse(token = token)
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: ""
            )
        }
    }
}