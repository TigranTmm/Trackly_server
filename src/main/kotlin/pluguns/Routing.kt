package com.example.pluguns

import com.example.data.repository.SessionRepositoryImpl
import com.example.data.repository.SphereRepositoryImpl
import com.example.data.repository.TaskRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.service.AnalyticsService
import com.example.domain.service.AuthService
import com.example.domain.service.SessionService
import com.example.domain.service.SphereService
import com.example.domain.service.TaskService
import com.example.routes.analyticsRoutes
import com.example.routes.authRoutes
import com.example.routes.sessionRoutes
import com.example.routes.sphereRoutes
import com.example.routes.taskRoutes
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

    val taskRepository = TaskRepositoryImpl()
    val taskService = TaskService(
        taskRepository = taskRepository,
        sphereRepository = sphereRepository
    )

    val sessionRepository = SessionRepositoryImpl()
    val sessionService = SessionService(
        sessionRepository = sessionRepository,
        sphereRepository = sphereRepository
    )

    val analyticsService = AnalyticsService(
        sessionRepository = sessionRepository,
        sphereRepository = sphereRepository
    )

    routing {
        authRoutes(authService)

        sphereRoutes(sphereService)

        taskRoutes(taskService)

        sessionRoutes(sessionService)

        analyticsRoutes(analyticsService)

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

            /** Testing existsByIdAndUserId method
            get("/sphere/{id}/") {
                try {
                    val userId = call.getUserId()
                    val sphereId = call.getIdFromRoute()

                    val isExist = sphereRepository.existsByIdAndUserId(userId, sphereId)

                    if (isExist) call.respond("Sphere${sphereId}IsExist")
                    else call.respond(HttpStatusCode.BadRequest, "SphereIsNotExist")
                } catch (e: IllegalArgumentException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        e.message ?: ""
                    )
                }
            }
            **/
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


/** Getting id from route **/
fun ApplicationCall.getIdFromRoute(tag: String): Long {
    val sphereId = parameters[tag]?.toLongOrNull()
        ?: throw IllegalArgumentException("InvalidToken")

    return sphereId
}
