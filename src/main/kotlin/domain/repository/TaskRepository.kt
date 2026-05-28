package com.example.domain.repository

import com.example.domain.models.Task
import java.time.Instant

interface TaskRepository {

    suspend fun createTask(
        sphereId: Long,
        content: String,
        priority: String,
        isCompleted: Boolean,
        completedAt: Instant?
    ): Task

    suspend fun getAllTasksOfSphere(sphereId: Long): List<Task>

    suspend fun getTaskById(sphereId: Long, taskId: Long): Task?

    suspend fun updateTask(
        sphereId: Long,
        taskId: Long,
        newContent: String,
        newPriority: String,
        newIsCompleted: Boolean,
        newCompletedAt: Instant?,
    ): Task?

    suspend fun deleteTask(sphereId: Long, taskId: Long): Boolean
}
