package com.example.dto.sphere

import kotlinx.serialization.Serializable

@Serializable
data class SphereResponse(
    val id: Long,
    val title: String,
    val colorKey: String,
    val iconKey: String,
    val hasTasks: Boolean,
)
