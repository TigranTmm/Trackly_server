package com.example.domain.repository

import com.example.domain.models.Sphere

interface SphereRepository {

    suspend fun createSphere(
        userId: Long,
        title: String,
        colorKey: String,
        iconKey: String,
        hasTasks: Boolean
    ): Sphere

    suspend fun getUserSpheres(userId: Long): List<Sphere>

    suspend fun getSphereById(userId: Long, sphereId: Long): Sphere?

    suspend fun updateSphere(
        userId: Long,
        sphereId: Long,
        newTitle: String,
        newColorKey: String,
        newIconKey: String,
        newHasTasks: Boolean
    ): Sphere?

    suspend fun deleteSphere(userId: Long, sphereId: Long): Boolean
}
