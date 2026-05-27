package com.example.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RequestResponce (
    val id: Long,
    val email: String
)