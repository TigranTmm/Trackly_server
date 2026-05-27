package com.example.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
