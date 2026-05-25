package com.example.domain.repository

import com.example.domain.models.User

interface UserRepository {
    suspend fun createUser (
        email: String,
        passwordHash: String
    ): User

    suspend fun existsByEmail(email: String): Boolean

    suspend fun getByEmail(email: String): User?
}