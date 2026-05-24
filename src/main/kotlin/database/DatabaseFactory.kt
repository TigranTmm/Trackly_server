package com.example.database

import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {

    fun init(environment: ApplicationEnvironment) {
        Database.connect(
            url = environment.config
                .property("database.url")
                .getString(),
            driver = environment.config
                .property("database.driver")
                .getString(),
            user = environment.config
                .property("database.user")
                .getString(),
            password = environment.config
                .property("database.password")
                .getString()
        )

        println("Database connected")
    }
}