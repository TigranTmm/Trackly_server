package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequestResponce (
    val id: Long,
    val email: String
)