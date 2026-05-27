package com.example.data.repository

import com.example.data.tables.UsersTable
import com.example.domain.models.User
import com.example.domain.repository.UserRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(
        email: String,
        passwordHash: String
    ): User {
        return transaction {
            val row = UsersTable.insert {
                it[UsersTable.email] = email
                it[UsersTable.passwordHash] = passwordHash
            }

            User(
                id = row[UsersTable.userId],
                email = row[UsersTable.email],
                passwordHash = row[UsersTable.passwordHash],
                login = row[UsersTable.login],
                createdAt = row[UsersTable.createdAt]
            )
        }
    }

    override suspend fun existsByEmail(email: String): Boolean {
        return transaction {
            UsersTable
                .selectAll()
                .where { UsersTable.email eq email }
                .count() > 0
        }
    }

    override suspend fun getByEmail(email: String): User? {
        return transaction {
            val row = UsersTable
                .selectAll()
                .where { UsersTable.email eq email }
                .singleOrNull()

            row?.let {
                User(
                    id = it[UsersTable.userId],
                    email = it[UsersTable.email],
                    passwordHash = it[UsersTable.passwordHash],
                    login = it[UsersTable.login],
                    createdAt = it[UsersTable.createdAt]
                )
            }
        }
    }
}