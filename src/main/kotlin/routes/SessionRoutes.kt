package com.example.routes

import com.example.domain.models.toResponse
import com.example.domain.service.SessionService
import com.example.dto.session.CreateSessionRequest
import com.example.dto.session.FinishSessionRequest
import com.example.pluguns.getIdFromRoute
import com.example.pluguns.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

fun Route.sessionRoutes(
    sessionService: SessionService
) {
    authenticate {

        // Adding session
        post("/spheres/{id}/sessions") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")

                val request = call.receive<CreateSessionRequest>()

                val session = sessionService.createSession(
                    userId = userId,
                    sphereId = sphereId,
                    title = request.title,
                    planDurationSeconds = request.planDurationSeconds
                )

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting all sessions
        get("/spheres/{id}/sessions") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")

                call.respond(
                    sessionService.getAllSessionsOfSphere(userId, sphereId)
                        .map { it.toResponse() }
                )

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting session by id
        get("/spheres/{id}/sessions/{sessionId}") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                val session = sessionService.getSessionById(userId, sphereId, sessionId)

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Deleting session
        delete("/spheres/{id}/sessions/{sessionId}") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                sessionService.deleteSession(userId, sphereId, sessionId)

                call.respond(HttpStatusCode.NoContent)

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting weekly sessions of sphere
        get("/spheres/{id}/sessions/week") {
            try {

                // Getting user id
                val userId = call.getUserId()

                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")

                val startOfWeek = Instant.now()
                    .atZone(ZoneOffset.UTC)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toInstant()

                val endOfWeek = Instant.now()
                    .atZone(ZoneOffset.UTC)
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(999999999)
                    .toInstant()

                val sessions = sessionService.getSessionsBetween(
                    userId = userId,
                    sphereId = sphereId,
                    start = startOfWeek,
                    end = endOfWeek
                )

                call.respond(
                    sessions.map { it.toResponse() }
                )

            } catch (e: IllegalArgumentException) {

                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }


        /** Work with session states **/

        // Starting session when status is PAUSED || CREATED
        post("/spheres/{id}/sessions/{sessionId}/start") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                val session = sessionService.startSession(userId, sphereId, sessionId)

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Pausing session when status is ACTIVE
        post("/spheres/{id}/sessions/{sessionId}/pause") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                val session = sessionService.pauseSession(userId, sphereId, sessionId)

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Finishing session when status is ACTIVE || PAUSED
        post("/spheres/{id}/sessions/{sessionId}/finish") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                val request = call.receive<FinishSessionRequest>()

                val session = sessionService.finishSession(
                    userId, sphereId, sessionId,
                    request.comment
                )

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Canceling session
        post("/spheres/{id}/sessions/{sessionId}/cancel") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting session id
                val sessionId = call.getIdFromRoute("sessionId")

                val session = sessionService.cancelSession(userId, sphereId, sessionId)

                call.respond(session.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

    }
}