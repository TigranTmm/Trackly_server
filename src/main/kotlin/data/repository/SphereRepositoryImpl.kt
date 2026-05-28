package com.example.data.repository

import com.example.data.tables.SpheresTable
import com.example.domain.models.Sphere
import com.example.domain.repository.SphereRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class SphereRepositoryImpl : SphereRepository {

    override suspend fun createSphere(
        userId: Long,
        title: String,
        colorKey: String,
        iconKey: String,
        hasTasks: Boolean
    ): Sphere {
        return transaction {
            val row = SpheresTable.insert {
                it[SpheresTable.userId] = userId
                it[SpheresTable.title] = title
                it[SpheresTable.colorKey] = colorKey
                it[SpheresTable.iconKey] = iconKey
                it[SpheresTable.hasTasks] = hasTasks
            }

            Sphere(
                id = row[SpheresTable.sphereId],
                userId = row[SpheresTable.userId],
                title = row[SpheresTable.title],
                colorKey = row[SpheresTable.colorKey],
                iconKey = row[SpheresTable.iconKey],
                hasTasks = row[SpheresTable.hasTasks],
                createdAt = row[SpheresTable.createdAt]
            )
        }
    }

    override suspend fun getUserSpheres(userId: Long): List<Sphere> {
        return transaction {
            SpheresTable
                .selectAll()
                .where { SpheresTable.userId eq userId }
                .map {
                    Sphere(
                        id = it[SpheresTable.sphereId],
                        userId = it[SpheresTable.userId],
                        title = it[SpheresTable.title],
                        colorKey = it[SpheresTable.colorKey],
                        iconKey = it[SpheresTable.iconKey],
                        hasTasks = it[SpheresTable.hasTasks],
                        createdAt = it[SpheresTable.createdAt]
                    )
                }
        }
    }

    override suspend fun getSphereById(userId: Long, sphereId: Long): Sphere? {
        return transaction {
            val row = SpheresTable
                .selectAll()
                .where { (SpheresTable.userId eq userId) and (SpheresTable.sphereId eq sphereId) }
                .singleOrNull() ?: return@transaction null

            Sphere(
                id = row[SpheresTable.sphereId],
                userId = row[SpheresTable.userId],
                title = row[SpheresTable.title],
                colorKey = row[SpheresTable.colorKey],
                iconKey = row[SpheresTable.iconKey],
                hasTasks = row[SpheresTable.hasTasks],
                createdAt = row[SpheresTable.createdAt]
            )
        }
    }

    override suspend fun updateSphere(
        userId: Long,
        sphereId: Long,
        newTitle: String,
        newColorKey: String,
        newIconKey: String,
        newHasTasks: Boolean
    ): Sphere? {
        return transaction {
            val updatedRows = SpheresTable
                .update(
                    where = { (SpheresTable.userId eq userId) and (SpheresTable.sphereId eq sphereId) }
                ) {
                    it[SpheresTable.title] = newTitle
                    it[SpheresTable.colorKey] = newColorKey
                    it[SpheresTable.iconKey] = newIconKey
                    it[SpheresTable.hasTasks] = newHasTasks
                }

            if (updatedRows == 0) return@transaction null

            val row = SpheresTable
                .selectAll()
                .where { (SpheresTable.userId eq userId) and (SpheresTable.sphereId eq sphereId) }
                .single()

            Sphere(
                id = row[SpheresTable.sphereId],
                userId = row[SpheresTable.userId],
                title = row[SpheresTable.title],
                colorKey = row[SpheresTable.colorKey],
                iconKey = row[SpheresTable.iconKey],
                hasTasks = row[SpheresTable.hasTasks],
                createdAt = row[SpheresTable.createdAt]
            )
        }
    }

    override suspend fun deleteSphere(userId: Long, sphereId: Long): Boolean {
        return transaction {
            val row = SpheresTable
                .deleteWhere {
                    (SpheresTable.userId eq userId) and (SpheresTable.sphereId eq sphereId)
                }

            row > 0
        }
    }

    override suspend fun existsByIdAndUserId(userId: Long, sphereId: Long): Boolean {
        return transaction {
            val row = SpheresTable
                .selectAll()
                .where { (SpheresTable.userId eq userId) and (SpheresTable.sphereId eq sphereId) }
                .singleOrNull()

            row != null
        }
    }
}