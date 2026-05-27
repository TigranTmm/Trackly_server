package com.example.domain.service

import com.example.domain.models.Sphere
import com.example.domain.repository.SphereRepository

class SphereService(
    private val repository: SphereRepository
) {

    suspend fun createSphere(
        userId: Long,
        title: String,
        colorKey: String,
        iconKey: String,
        hasTasks: Boolean
    ): Sphere {
        // Check if title is correct
        val checkedTitle = title.trim()
        if(checkedTitle.isEmpty() || checkedTitle.length > 50) throw IllegalArgumentException("TitleIsIncorrect")

        return repository.createSphere(
            userId = userId,
            title = checkedTitle,
            colorKey = colorKey,
            iconKey = iconKey,
            hasTasks = hasTasks
        )
    }

    suspend fun getUserSpheres(userId: Long): List<Sphere> {
        return repository.getUserSpheres(userId)
    }

    suspend fun getSphereById(userId: Long, sphereId: Long): Sphere {
        // Check if sphere is existing
        val sphere = repository.getSphereById(userId, sphereId)
            ?: throw IllegalArgumentException("SphereIsNotExist")

        return sphere
    }

    suspend fun updateSphere(
        userId: Long,
        sphereId: Long,
        newTitle: String,
        newColorKey: String,
        newIconKey: String,
        newHasTasks: Boolean
    ): Sphere {
        // Check if title is correct
        val checkedTitle = newTitle.trim()
        if(checkedTitle.isEmpty() || checkedTitle.length > 50) throw IllegalArgumentException("TitleIsIncorrect")

        val newSphere = repository.updateSphere(
            userId = userId,
            sphereId = sphereId,
            newTitle = checkedTitle,
            newColorKey = newColorKey,
            newIconKey = newIconKey,
            newHasTasks = newHasTasks
        ) ?: throw IllegalArgumentException("SphereIsNotExist")

        return newSphere
    }

    suspend fun deleteSphere(userId: Long, sphereId: Long): Boolean {
        val isDeleted = repository.deleteSphere(userId, sphereId)

        if (!isDeleted) throw IllegalArgumentException("SphereWasNotDeleted")

        return true
    }
}
