package com.example.data.repository

import com.example.data.tables.SessionsTable
import com.example.data.tables.SpheresTable
import com.example.domain.models.Session
import com.example.domain.models.SessionStatus
import com.example.domain.repository.SessionRepository
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class SessionRepositoryImpl : SessionRepository {

    override suspend fun createSession(
        sphereId: Long,
        title: String,
        comment: String?,
        status: SessionStatus,
        planDurationSeconds: Int,
        factualDurationSeconds: Int?,
        pausedSeconds: Int,
        pauseStartedAt: Instant?,
        startedAt: Instant?,
        endedAt: Instant?
    ): Session {
        return transaction {
            val row = SessionsTable.insert {
                it[SessionsTable.sphereId] = sphereId
                it[SessionsTable.title] = title
                it[SessionsTable.comment] = comment
                it[SessionsTable.status] = status.name
                it[SessionsTable.planDurationSeconds] = planDurationSeconds
                it[SessionsTable.factualDurationSeconds] = factualDurationSeconds
                it[SessionsTable.pausedSeconds] = pausedSeconds
                it[SessionsTable.pauseStartedAt] = pauseStartedAt
                it[SessionsTable.startedAt] = startedAt
                it[SessionsTable.endedAt] = endedAt
            }

            Session(
                id = row[SessionsTable.sessionId],
                sphereId = row[SessionsTable.sphereId],
                title = row[SessionsTable.title],
                comment = row[SessionsTable.comment],
                status = SessionStatus.valueOf(row[SessionsTable.status]),
                planDurationSeconds = row[SessionsTable.planDurationSeconds],
                factualDurationSeconds = row[SessionsTable.factualDurationSeconds],
                pausedSeconds = row[SessionsTable.pausedSeconds],
                pauseStartedAt = row[SessionsTable.pauseStartedAt],
                startedAt = row[SessionsTable.startedAt],
                endedAt = row[SessionsTable.endedAt],
                createdAt = row[SessionsTable.createdAt]
            )
        }
    }

    override suspend fun getAllSessionsOfSphere(sphereId: Long): List<Session> {
        return transaction {
            SessionsTable
                .selectAll()
                .where { SessionsTable.sphereId eq sphereId }
                .orderBy(SessionsTable.createdAt to SortOrder.DESC)
                .map { it.toSession() }
        }
    }

    override suspend fun getSessionById(
        sphereId: Long,
        sessionId: Long
    ): Session? {
        return transaction {
            val row = SessionsTable
                .selectAll()
                .where { (SessionsTable.sphereId eq sphereId) and (SessionsTable.sessionId eq sessionId) }
                .singleOrNull() ?: return@transaction null

            row.toSession()
        }

    }

    override suspend fun updateSession(
        sphereId: Long,
        sessionId: Long,
        newTitle: String,
        newComment: String?,
        newStatus: SessionStatus,
        newPlanDurationSeconds: Int,
        newFactualDurationSeconds: Int?,
        newPausedSeconds: Int,
        newPauseStartedAt: Instant?,
        newStartedAt: Instant?,
        newEndedAt: Instant?,
    ): Session {
        return transaction {
            SessionsTable.update(
                where = { (SessionsTable.sphereId eq sphereId) and (SessionsTable.sessionId eq sessionId) }
            ) {
                it[SessionsTable.title] = newTitle
                it[SessionsTable.comment] = newComment
                it[SessionsTable.status] = newStatus.name
                it[SessionsTable.planDurationSeconds] = newPlanDurationSeconds
                it[SessionsTable.factualDurationSeconds] = newFactualDurationSeconds
                it[SessionsTable.pausedSeconds] = newPausedSeconds
                it[SessionsTable.pauseStartedAt] = newPauseStartedAt
                it[SessionsTable.startedAt] = newStartedAt
                it[SessionsTable.endedAt] = newEndedAt
            }

            val row = SessionsTable
                .selectAll()
                .where { (SessionsTable.sphereId eq sphereId) and (SessionsTable.sessionId eq sessionId) }
                .single()

            row.toSession()
        }
    }

    override suspend fun deleteSession(sphereId: Long, sessionId: Long): Boolean {
        return transaction {
            val row = SessionsTable
                .deleteWhere {
                    (SessionsTable.sphereId eq sphereId) and (SessionsTable.sessionId eq sessionId)
                }

            row > 0
        }
    }

    override suspend fun getSessionsBetween(
        sphereId: Long,
        start: Instant,
        end: Instant
    ): List<Session> {
        return transaction {
            SessionsTable
                .selectAll()
                .where {
                    (SessionsTable.sphereId eq sphereId) and
                            (SessionsTable.status eq SessionStatus.COMPLETED.name) and
                            (SessionsTable.startedAt.between(start, end))
                }
                .orderBy(SessionsTable.createdAt to SortOrder.DESC)
                .map { it.toSession() }
        }
    }

    override suspend fun getUserSessionsBetween(
        userId: Long,
        start: Instant,
        end: Instant
    ): List<Session> {
        return transaction {
            SessionsTable
                .innerJoin(SpheresTable)
                .selectAll()
                .where {
                    (SpheresTable.userId eq userId) and
                            (SessionsTable.status eq SessionStatus.COMPLETED.name) and
                            (SessionsTable.startedAt.between(start, end))
                }
                .orderBy(SessionsTable.startedAt to SortOrder.DESC)
                .map { it.toSession() }
        }
    }
}