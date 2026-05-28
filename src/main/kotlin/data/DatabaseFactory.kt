package com.example.data

import com.example.data.tables.SpheresTable
import com.example.data.tables.TasksTable
import com.example.data.tables.UsersTable
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

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

        transaction {
            SchemaUtils.create(UsersTable, SpheresTable, TasksTable)
        }

        println("Database connected")
    }
}