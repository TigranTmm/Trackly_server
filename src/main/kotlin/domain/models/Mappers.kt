package com.example.domain.models

import com.example.dto.session.SessionResponse
import com.example.dto.sphere.SphereResponse
import com.example.dto.task.TaskResponse

/** Mapping Sphere to SphereResponse **/
fun Sphere.toResponse(): SphereResponse {
    return SphereResponse(
        id = id,
        title = title,
        colorKey = colorKey,
        iconKey = iconKey,
        hasTasks = hasTasks
    )
}


/** Mapping Task to TaskResponse **/
fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        content = content,
        priority = priority,
        isCompleted = isCompleted
    )
}


/** Mapping Session to SessionResponse **/
fun Session.toResponse(): SessionResponse {
    return SessionResponse(
        id = id,
        sphereId = sphereId,
        title = title,
        comment = comment,
        status = status.name,
        planDurationSeconds = planDurationSeconds,
        factualDurationSeconds = factualDurationSeconds,
        pausedSeconds = pausedSeconds,
        startedAt = startedAt?.toString(),
        endedAt = endedAt?.toString()
    )
}
