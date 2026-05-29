package com.example.dto.session

import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequest(
    val title: String,
    val planDurationSeconds: Int
)
