package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object SessionsTable : Table("sessions") {
    val sessionId = long("session_id")
        .autoIncrement()

    val sphereId = reference("sphere_id", SpheresTable.sphereId)

    val title = varchar("title", 50)

    val comment = varchar("comment", 250)
        .nullable()

    val status = varchar("status", 15)

    val planDurationSeconds = integer("plan_duration_seconds")

    val factualDurationSeconds = integer("factual_duration_seconds")
        .nullable()

    val pausedSeconds = integer("paused_seconds")
        .default(0)

    val pauseStartedAt = timestamp("pause_started_at")
        .nullable()

    val startedAt = timestamp("started_at")
        .nullable()

    val endedAt = timestamp("ended_at")
        .nullable()

    val createdAt = timestamp("created_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(SessionsTable.sessionId)
}