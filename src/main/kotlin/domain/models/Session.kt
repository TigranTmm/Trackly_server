package com.example.domain.models

import java.time.Instant

data class Session(
    val id: Long,
    val sphereId: Long,
    val title: String,
    val comment: String?,
    val status: SessionStatus,
    val planDurationSeconds: Int,
    val factualDurationSeconds: Int?,
    val pausedSeconds: Int,
    val pauseStartedAt: Instant?,
    val startedAt: Instant?,
    val endedAt: Instant?,
    val createdAt: Instant
)
