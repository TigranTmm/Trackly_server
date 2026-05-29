package com.example.domain.service

import com.example.domain.models.User
import com.example.domain.repository.UserRepository
import com.example.domain.security.JWTConfig
import com.example.domain.security.PasswordHasher

class AuthService(
    private val userRepository: UserRepository
) {

    suspend fun register(
        email: String,
        password: String
    ): User {
        val exists = userRepository.existsByEmail(email)

        // Check if the email exists
        if (exists) throw IllegalArgumentException("UserWithThisEmailAlreadyExists")

        return userRepository.createUser(
            email = email,
            passwordHash = PasswordHasher.hash(password)
        )
    }

    suspend fun login(
        email: String,
        password: String
    ): String {
        // Check if the user exists
        val user = userRepository.getByEmail(email)
            ?: throw IllegalArgumentException("UserWithThisEmailDoesNotExist")

        // Check if the password correct
        val isPasswordCorrect = PasswordHasher.verify(password, user.passwordHash)
        if (!isPasswordCorrect) throw IllegalArgumentException("PasswordIsIncorrect")

        val token = JWTConfig.generateToken(user.id)

        return token
    }

}