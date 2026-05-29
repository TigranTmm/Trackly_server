package com.example.domain.service

import com.example.domain.models.Session
import com.example.domain.models.SessionStatus
import com.example.domain.repository.SessionRepository
import com.example.domain.repository.SphereRepository
import java.time.Duration
import java.time.Instant

class SessionService(
    private val sessionRepository: SessionRepository,
    private val sphereRepository: SphereRepository
) {

    suspend fun createSession(
        userId: Long,
        sphereId: Long,
        title: String,
        planDurationSeconds: Int
    ): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if title is valid
        val checkedTitle = title.trim()
        if (checkedTitle.isEmpty() || checkedTitle.length > 50) throw IllegalArgumentException("TitleIsInvalid")

        // Check if planned duration is valid
        if (planDurationSeconds <= 0) throw IllegalArgumentException("DurationIsInvalid")

        return sessionRepository.createSession(
            sphereId = sphereId,
            title = checkedTitle,
            comment = null,
            status = SessionStatus.CREATED,
            planDurationSeconds = planDurationSeconds,
            factualDurationSeconds = null,
            pausedSeconds = 0,
            pauseStartedAt = null,
            startedAt = null,
            endedAt = null
        )
    }

    suspend fun getAllSessionsOfSphere(userId: Long, sphereId: Long): List<Session> {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        return sessionRepository.getAllSessionsOfSphere(sphereId)
    }

    suspend fun getSessionById(userId: Long, sphereId: Long, sessionId: Long): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if session is existing
        val session = sessionRepository.getSessionById(sphereId, sessionId)
            ?: throw IllegalArgumentException("SessionIsNotExist")

        return session
    }

    suspend fun deleteSession(userId: Long, sphereId: Long, sessionId: Long): Boolean {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        val isDeleted = sessionRepository.deleteSession(sphereId, sessionId)

        if (!isDeleted) throw IllegalArgumentException("SessionWasNotDeleted")

        return true
    }


    /** Work with session states **/

    // Starting session when status is PAUSED || CREATED
    suspend fun startSession(userId: Long, sphereId: Long, sessionId: Long): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if session is existing
        var session = sessionRepository.getSessionById(sphereId, sessionId)
            ?: throw IllegalArgumentException("SessionIsNotExist")

        if (session.status == SessionStatus.PAUSED || session.status == SessionStatus.CREATED) {
            var additionalPausedSeconds = 0

            if(session.status == SessionStatus.PAUSED) {
                val pauseStartedAt = session.pauseStartedAt
                    ?: throw IllegalStateException("PauseStartedAtIsNull")

                additionalPausedSeconds = Duration.between(
                    pauseStartedAt,
                    Instant.now()
                ).seconds.toInt()
            }

            session = sessionRepository.updateSession(
                sphereId = sphereId,
                sessionId = sessionId,
                newTitle = session.title,
                newComment = session.comment,
                newStatus = SessionStatus.ACTIVE,
                newPlanDurationSeconds = session.planDurationSeconds,
                newFactualDurationSeconds = session.factualDurationSeconds,
                newPausedSeconds = session.pausedSeconds + additionalPausedSeconds,
                newPauseStartedAt = null,
                newStartedAt = session.startedAt ?: Instant.now(),
                newEndedAt = session.endedAt
            )
        } else throw IllegalArgumentException("SessionStatusIsIncorrect")

        return session
    }

    // Pausing session when status is ACTIVE
    suspend fun pauseSession(userId: Long, sphereId: Long, sessionId: Long): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if session is existing
        var session = sessionRepository.getSessionById(sphereId, sessionId)
            ?: throw IllegalArgumentException("SessionIsNotExist")

        if (session.status == SessionStatus.ACTIVE) {
            session = sessionRepository.updateSession(
                sphereId = sphereId,
                sessionId = sessionId,
                newTitle = session.title,
                newComment = session.comment,
                newStatus = SessionStatus.PAUSED,
                newPlanDurationSeconds = session.planDurationSeconds,
                newFactualDurationSeconds = session.factualDurationSeconds,
                newPausedSeconds = session.pausedSeconds,
                newPauseStartedAt = Instant.now(),
                newStartedAt = session.startedAt,
                newEndedAt = session.endedAt
            )
        } else throw IllegalArgumentException("SessionStatusIsIncorrect")

        return session
    }

    // Finishing session when status is ACTIVE || PAUSED
    suspend fun finishSession(
        userId: Long, sphereId: Long, sessionId: Long,
        comment: String?
    ): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if session is existing
        var session = sessionRepository.getSessionById(sphereId, sessionId)
            ?: throw IllegalArgumentException("SessionIsNotExist")

        if (session.status == SessionStatus.ACTIVE || session.status == SessionStatus.PAUSED) {
            var totalPausedSeconds = session.pausedSeconds

            if (session.status == SessionStatus.PAUSED) {
                totalPausedSeconds += Duration.between(
                    session.pauseStartedAt,
                    Instant.now()
                ).seconds.toInt()
            }

            val startedAt = session.startedAt
                ?: throw IllegalStateException("StartedAtIsNull")

            val factualDuration = Duration.between(
                startedAt,
                Instant.now()
            ).seconds.toInt() - totalPausedSeconds

            session = sessionRepository.updateSession(
                sphereId = sphereId,
                sessionId = sessionId,
                newTitle = session.title,
                newComment = comment,
                newStatus = SessionStatus.COMPLETED,
                newPlanDurationSeconds = session.planDurationSeconds,
                newFactualDurationSeconds = factualDuration,
                newPausedSeconds = totalPausedSeconds,
                newPauseStartedAt = null,
                newStartedAt = session.startedAt,
                newEndedAt = Instant.now()
            )
        } else throw IllegalArgumentException("SessionStatusIsIncorrect")

        return session
    }

    // Canceling session
    suspend fun cancelSession(userId: Long, sphereId: Long, sessionId: Long): Session {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if session is existing
        var session = sessionRepository.getSessionById(sphereId, sessionId)
            ?: throw IllegalArgumentException("SessionIsNotExist")

        if (session.status == SessionStatus.COMPLETED || session.status == SessionStatus.CANCELED) {
            throw IllegalArgumentException("SessionStatusIsIncorrect")
        }

        session = sessionRepository.updateSession(
            sphereId = sphereId,
            sessionId = sessionId,
            newTitle = session.title,
            newComment = session.comment,
            newStatus = SessionStatus.CANCELED,
            newPlanDurationSeconds = session.planDurationSeconds,
            newFactualDurationSeconds = session.factualDurationSeconds,
            newPausedSeconds = session.pausedSeconds,
            newPauseStartedAt = null,
            newStartedAt = session.startedAt,
            newEndedAt = Instant.now()
        )

        return session
    }
}