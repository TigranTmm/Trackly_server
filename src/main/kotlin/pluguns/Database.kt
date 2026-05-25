package com.example.pluguns

import com.example.data.DatabaseFactory
import io.ktor.server.application.Application

fun Application.configureDatabase() {
    DatabaseFactory.init(environment)
}