package com.example.dto.session

import kotlinx.serialization.Serializable

@Serializable
data class FinishSessionRequest(
    val comment: String?
)
