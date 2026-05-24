package com.example.pluguns

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Test (
    val text: String
)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(listOf(
                Test("Hello, World!"),
                Test("AAAAAAAAAAAA")
            ))
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}