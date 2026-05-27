package com.example.pluguns

import com.example.data.repository.SphereRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.service.AuthService
import com.example.domain.service.SphereService
import com.example.routes.authRoutes
import com.example.routes.sphereRoutes
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

    val sphereRepository = SphereRepositoryImpl()
    val sphereService = SphereService(sphereRepository)

    routing {
        authRoutes(authService)

        sphereRoutes(sphereService)

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


/** Getting user id **/
fun ApplicationCall.getUserId(): Long {

    val principal = principal<JWTPrincipal>()

    return principal
        ?.payload
        ?.getClaim("userId")
        ?.asLong()
        ?: throw IllegalArgumentException("InvalidToken")
}
