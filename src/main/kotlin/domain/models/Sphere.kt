package com.example.domain.models

import java.time.Instant

data class Sphere(
    val id: Long,
    val userId: Long,
    val title: String,
    val colorKey: String,
    val iconKey: String,
    val hasTasks: Boolean,
    val createdAt: Instant
)
