package com.example.dto.analytics

import kotlinx.serialization.Serializable

@Serializable
data class SphereActivityItem(
    val sphereId: Long,
    val sphereTitle: String,
    val sphereColor: String,
    val sphereIcon: String,
    val totalSeconds: Int
)
