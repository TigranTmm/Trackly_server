package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val password: String
)