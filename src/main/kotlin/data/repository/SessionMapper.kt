package com.example.data.repository

import com.example.data.tables.SessionsTable
import com.example.domain.models.Session
import com.example.domain.models.SessionStatus
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toSession(): Session {
    return Session(
        id = this[SessionsTable.sessionId],
        sphereId = this[SessionsTable.sphereId],
        title = this[SessionsTable.title],
        comment = this[SessionsTable.comment],
        status = SessionStatus.valueOf(this[SessionsTable.status]),
        planDurationSeconds = this[SessionsTable.planDurationSeconds],
        factualDurationSeconds = this[SessionsTable.factualDurationSeconds],
        pausedSeconds = this[SessionsTable.pausedSeconds],
        pauseStartedAt = this[SessionsTable.pauseStartedAt],
        startedAt = this[SessionsTable.startedAt],
        endedAt = this[SessionsTable.endedAt],
        createdAt = this[SessionsTable.createdAt]
    )
}