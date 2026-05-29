package com.example.dto.session

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val id: Long,
    val title: String,
    val comment: String?,
    val status: String,
    val planDurationSeconds: Int,
    val factualDurationSeconds: Int?,
    val pausedSeconds: Int,
    val startedAt: String?,
    val endedAt: String?
)
