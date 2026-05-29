package com.example.routes

import com.example.domain.service.AnalyticsService
import com.example.pluguns.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.analyticsRoutes(
    analyticsService: AnalyticsService
) {
    authenticate {
        get("/analytics/weekly") {
            try {
                val userId = call.getUserId()

                val analytics = analyticsService.weeklyAnalytics(userId)

                call.respond(analytics)

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: ""
                )
            }
        }
    }
}
