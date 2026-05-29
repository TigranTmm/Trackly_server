package com.example.dto.analytics

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyAnalyticsResponse(
    val sphereActivity: List<SphereActivityItem>,
    val bestDay: DayStats?,
    val worstDay: DayStats?,
    val averageDaySeconds: Int,
    val longestSession: SessionPreview?,
    val streakDays: Int,
    val weekChart: List<SphereChartLine>
)