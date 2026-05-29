package com.example.dto.analytics

import kotlinx.serialization.Serializable

@Serializable
data class DayStats(
    val dayOfWeek: String,
    val totalSeconds: Int
)
