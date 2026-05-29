package com.example.domain.repository

import com.example.domain.models.Session
import com.example.domain.models.SessionStatus
import java.time.Instant

interface SessionRepository {

    suspend fun createSession(
        sphereId: Long,
        title: String,
        comment: String?,
        status: SessionStatus,
        planDurationSeconds: Int,
        factualDurationSeconds: Int?,
        pausedSeconds: Int,
        pauseStartedAt: Instant?,
        startedAt: Instant?,
        endedAt: Instant?,
    ): Session

    suspend fun getAllSessionsOfSphere(sphereId: Long): List<Session>

    suspend fun getSessionById(sphereId: Long, sessionId: Long): Session?

    suspend fun updateSession(
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
    ): Session

    suspend fun deleteSession(sphereId: Long, sessionId: Long): Boolean

    suspend fun getSessionsBetween(
        sphereId: Long,
        start: Instant,
        end: Instant
    ): List<Session>

    suspend fun getUserSessionsBetween(
        userId: Long,
        start: Instant,
        end: Instant
    ): List<Session>
}