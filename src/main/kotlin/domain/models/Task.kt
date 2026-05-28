package com.example.domain.models

import java.time.Instant

data class Task (
    val id: Long,
    val sphereId: Long,
    val content: String,
    val priority: String,
    val isCompleted: Boolean,
    val completedAt: Instant?,
    val createdAt: Instant
)
