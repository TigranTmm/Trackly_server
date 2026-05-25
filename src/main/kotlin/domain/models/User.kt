package com.example.domain.models

import java.time.Instant

data class User(
    val id: Long,
    val email: String,
    val passwordHash: String,
    val login: String?,
    val createdAt: Instant
)
