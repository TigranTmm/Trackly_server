package com.example.routes

import com.example.domain.models.toResponse
import com.example.domain.service.SphereService
import com.example.dto.sphere.SphereRequest
import com.example.pluguns.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.sphereRoutes(
    sphereService: SphereService
) {
    authenticate {

        // Adding sphere
        post("/spheres") {
            try {
                // Getting iser id
                val userId = call.getUserId()

                val request = call.receive<SphereRequest>()

                // Creating sphere
                val sphere = sphereService.createSphere(
                    userId = userId,
                    title = request.title,
                    colorKey = request.colorKey,
                    iconKey = request.iconKey,
                    hasTasks = request.hasTasks
                )

                call.respond(sphere.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting all spheres
        get("/spheres") {
            try {
                // Getting iser id
                val userId = call.getUserId()

                call.respond(
                    sphereService.getUserSpheres(userId).map { it.toResponse() }
                )
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting sphere by id
        get("/spheres/{id}") {
            try {
                // Getting sphere id
                val sphereId = call.parameters["id"]?.toLongOrNull()
                if (sphereId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "InvalidSphereId"
                    )
                    return@get
                }

                // Getting iser id
                val userId = call.getUserId()

                val sphere = sphereService.getSphereById(userId, sphereId)

                call.respond(sphere.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }

        }

        // Updating sphere by id
        put("/spheres/{id}") {
            try {
                // Getting sphere id
                val sphereId = call.parameters["id"]?.toLongOrNull()
                if (sphereId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "InvalidSphereId"
                    )
                    return@put
                }

                // Getting iser id
                val userId = call.getUserId()

                val sphere = call.receive<SphereRequest>()

                val newSphere = sphereService.updateSphere(
                    userId = userId,
                    sphereId = sphereId,
                    newTitle = sphere.title,
                    newColorKey = sphere.colorKey,
                    newIconKey = sphere.iconKey,
                    newHasTasks = sphere.hasTasks
                )

                call.respond(newSphere.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }

        }

        // Deleting sphere by id
        delete("/spheres/{id}") {
            try {
                // Getting sphere id
                val sphereId = call.parameters["id"]?.toLongOrNull()
                if (sphereId == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "InvalidSphereId"
                    )
                    return@delete
                }

                // Getting iser id
                val userId = call.getUserId()

                sphereService.deleteSphere(userId, sphereId)

                call.respond(HttpStatusCode.NoContent)

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

    }
}
