package com.example.data.repository

import com.example.data.tables.TasksTable
import com.example.domain.models.Task
import com.example.domain.repository.TaskRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class TaskRepositoryImpl : TaskRepository {

    override suspend fun createTask(
        sphereId: Long,
        content: String,
        priority: String,
        isCompleted: Boolean,
        completedAt: Instant?
    ): Task {
        return transaction {
            val row = TasksTable.insert {
                it[TasksTable.sphereId] = sphereId
                it[TasksTable.content] = content
                it[TasksTable.priority] = priority
                it[TasksTable.isCompleted] = isCompleted
                it[TasksTable.completedAt] = null
            }

            Task (
                id = row[TasksTable.taskId],
                sphereId = row[TasksTable.sphereId],
                content = row[TasksTable.content],
                priority = row[TasksTable.priority],
                isCompleted = row[TasksTable.isCompleted],
                completedAt = row[TasksTable.completedAt],
                createdAt = row[TasksTable.createdAt]
            )
        }
    }

    override suspend fun getAllTasksOfSphere(sphereId: Long): List<Task> {
        return transaction {
            TasksTable
                .selectAll()
                .where { TasksTable.sphereId eq sphereId }
                .map {
                    Task (
                        id = it[TasksTable.taskId],
                        sphereId = it[TasksTable.sphereId],
                        content = it[TasksTable.content],
                        priority = it[TasksTable.priority],
                        isCompleted = it[TasksTable.isCompleted],
                        completedAt = it[TasksTable.completedAt],
                        createdAt = it[TasksTable.createdAt]
                    )
                }
        }
    }

    override suspend fun getTaskById(sphereId: Long, taskId: Long): Task? {
        return transaction {
            val row = TasksTable
                .selectAll()
                .where { (TasksTable.sphereId eq sphereId) and (TasksTable.taskId eq taskId) }
                .singleOrNull() ?: return@transaction null

            Task (
                id = row[TasksTable.taskId],
                sphereId = row[TasksTable.sphereId],
                content = row[TasksTable.content],
                priority = row[TasksTable.priority],
                isCompleted = row[TasksTable.isCompleted],
                completedAt = row[TasksTable.completedAt],
                createdAt = row[TasksTable.createdAt]
            )
        }
    }

    override suspend fun updateTask(
        sphereId: Long,
        taskId: Long,
        newContent: String,
        newPriority: String,
        newIsCompleted: Boolean,
        newCompletedAt: Instant?
    ): Task? {
        return transaction {
            val updatedRows = TasksTable.update(
                where = { (TasksTable.sphereId eq sphereId) and (TasksTable.taskId eq taskId) }
            ) {
                it[TasksTable.content] = newContent
                it[TasksTable.priority] = newPriority
                it[TasksTable.isCompleted] = newIsCompleted
                it[TasksTable.completedAt] = newCompletedAt
            }

            if (updatedRows == 0) return@transaction null

            val row = TasksTable
                .selectAll()
                .where { (TasksTable.sphereId eq sphereId) and (TasksTable.taskId eq taskId) }
                .single()

            Task (
                id = row[TasksTable.taskId],
                sphereId = row[TasksTable.sphereId],
                content = row[TasksTable.content],
                priority = row[TasksTable.priority],
                isCompleted = row[TasksTable.isCompleted],
                completedAt = row[TasksTable.completedAt],
                createdAt = row[TasksTable.createdAt]
            )
        }

    }

    override suspend fun deleteTask(sphereId: Long, taskId: Long): Boolean {
        return transaction {
            val row = TasksTable
                .deleteWhere {
                    (TasksTable.sphereId eq sphereId) and (TasksTable.taskId eq taskId)
                }

            row > 0
        }
    }
}