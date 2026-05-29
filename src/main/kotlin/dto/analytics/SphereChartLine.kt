package com.example.dto.analytics

import kotlinx.serialization.Serializable

@Serializable
data class SphereChartLine(
    val sphereId: Long,
    val sphereTitle: String,
    val sphereColor: String,

    val points: List<DayPoint>
)

@Serializable
data class DayPoint(
    val dayOfWeek: String,
    val totalSeconds: Int
)
