package com.example.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val password: String
)