package com.example.routes

import com.example.domain.models.toResponse
import com.example.domain.service.TaskService
import com.example.dto.task.TaskRequest
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

fun Route.taskRoutes(
    taskService: TaskService
) {
    authenticate {

        // Adding task
        post("/spheres/{id}/tasks") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")

                val request = call.receive<TaskRequest>()

                val task = taskService.createTask(
                    userId = userId,
                    sphereId = sphereId,
                    content = request.content,
                    priority = request.priority,
                    isCompleted = request.isCompleted
                )

                call.respond(task.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting all tasks
        get("/spheres/{id}/tasks") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")

                call.respond(
                    taskService.getAllTasksOfSphere(userId, sphereId)
                        .map { it.toResponse() }
                )

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Getting task by id
        get("/spheres/{id}/tasks/{taskId}") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting task id
                val taskId = call.getIdFromRoute("taskId")

                val task = taskService.getTaskById(userId, sphereId, taskId)

                call.respond(task.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Updating task
        put("/spheres/{id}/tasks/{taskId}") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting task id
                val taskId = call.getIdFromRoute("taskId")

                val request = call.receive<TaskRequest>()

                val newTask = taskService.updateTask(
                    userId = userId,
                    sphereId = sphereId,
                    taskId = taskId,
                    newContent = request.content,
                    newPriority = request.priority,
                    newIsCompleted = request.isCompleted
                )

                call.respond(newTask.toResponse())

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }

        // Deleting task
        delete("/spheres/{id}/tasks/{taskId}") {
            try {
                // Getting user id
                val userId = call.getUserId()
                // Getting sphere id
                val sphereId = call.getIdFromRoute("id")
                // Getting task id
                val taskId = call.getIdFromRoute("taskId")

                taskService.deleteTask(userId, sphereId, taskId)

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