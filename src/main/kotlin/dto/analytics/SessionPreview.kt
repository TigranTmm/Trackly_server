package com.example.dto.analytics

import kotlinx.serialization.Serializable

@Serializable
data class SessionPreview(
    val sessionId: Long,
    val sphereId: Long,
    val sphereTitle: String,
    val sphereIcon: String,
    val title: String,
    val factualDurationSeconds: Int,
    val endedAt: String,
    val comment: String?
)
