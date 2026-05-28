package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object TasksTable : Table("tasks") {
    val taskId = long("task_id")
        .autoIncrement()

    val sphereId = reference("sphere_id", SpheresTable.sphereId)

    val content = varchar("content", 100)

    val priority = varchar("priority", 10)

    val isCompleted = bool("is_completed")
        .default(false)

    val completedAt = timestamp("completed_at")
        .nullable()
        .default(null)

    val createdAt = timestamp("created_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(TasksTable.taskId)
}
