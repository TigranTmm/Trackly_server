package com.example.domain.service

import com.example.domain.models.Task
import com.example.domain.repository.SphereRepository
import com.example.domain.repository.TaskRepository
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import java.time.Instant

class TaskService(
    private val taskRepository: TaskRepository,
    private val sphereRepository: SphereRepository
) {

    suspend fun createTask(
        userId: Long,
        sphereId: Long,
        content: String,
        priority: String,
        isCompleted: Boolean,
    ): Task {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if content is valid
        val checkedContent = content.trim()
        if (content.isEmpty() || content.length > 100) throw IllegalArgumentException("TaskIsInvalid")

        return taskRepository.createTask(
            sphereId = sphereId,
            content = checkedContent,
            priority = priority,
            isCompleted = isCompleted,
            completedAt = null
        )
    }

    suspend fun getAllTasksOfSphere(userId: Long, sphereId: Long): List<Task> {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        return taskRepository.getAllTasksOfSphere(sphereId)
    }

    suspend fun getTaskById(userId: Long, sphereId: Long, taskId: Long): Task {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if task is existing
        val task = taskRepository.getTaskById(sphereId, taskId)
            ?: throw IllegalArgumentException("TaskIsNotExist")

        return task
    }

    suspend fun updateTask(
        userId: Long,
        sphereId: Long,
        taskId: Long,
        newContent: String,
        newPriority: String,
        newIsCompleted: Boolean,
    ): Task {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        // Check if content is valid
        val checkedContent = newContent.trim()
        if (newContent.isEmpty() || newContent.length > 100) throw IllegalArgumentException("TaskIsInvalid")

        val task = taskRepository.updateTask(
            sphereId = sphereId,
            taskId = taskId,
            newContent = checkedContent,
            newPriority = newPriority,
            newIsCompleted = newIsCompleted,
            newCompletedAt = if (newIsCompleted) Instant.now() else null
        ) ?: throw IllegalArgumentException("TaskIsNotExist")

        return task
    }

    suspend fun deleteTask(userId: Long, sphereId: Long, taskId: Long): Boolean {
        // Check if sphere is existing
        val isSphereValid = sphereRepository.existsByIdAndUserId(userId, sphereId)
        if (!isSphereValid) throw IllegalArgumentException("SphereIsNotExist")

        val isDeleted = taskRepository.deleteTask(sphereId, taskId)

        if (!isDeleted) throw IllegalArgumentException("TaskWasNotDeleted")

        return true
    }
}
