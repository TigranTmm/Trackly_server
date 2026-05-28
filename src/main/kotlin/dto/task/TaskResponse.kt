package com.example.dto.task

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse (
    val content: String,
    val priority: String,
    val isCompleted: Boolean
)