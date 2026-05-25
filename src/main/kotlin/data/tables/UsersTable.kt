package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object UsersTable : Table("users") {
    val userId = long("user_id")
        .autoIncrement()

    val email = varchar("email", length = 255)
        .uniqueIndex()

    val passwordHash = varchar("password_hash", length = 255)

    val login = varchar("login", length = 50)
        .nullable()
        .uniqueIndex()

    val createdAt = timestamp("created_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(userId)
}