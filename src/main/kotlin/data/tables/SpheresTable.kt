package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestamp
import org.jetbrains.exposed.v1.javatime.timestamp

object SpheresTable : Table("spheres") {
    val sphereId = long("sphere_id")
        .autoIncrement()

    val userId = reference("user_id", UsersTable.userId)

    val title = varchar("title", 50)

    val colorKey = varchar("color_key", 50)

    val iconKey = varchar("icon_key", 50)

    val hasTasks = bool("has_tasks")
        .default(true)

    val createdAt = timestamp("created_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(sphereId)
}