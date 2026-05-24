package com.example.pluguns

import com.example.database.DatabaseFactory
import io.ktor.server.application.Application

fun Application.configureDatabase() {
    DatabaseFactory.init(environment)
}