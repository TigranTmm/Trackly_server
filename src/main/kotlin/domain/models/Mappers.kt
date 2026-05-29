package com.example.domain.models

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


/** Mapping Sphere to SphereResponse **/
fun Task.toResponse(): TaskResponse {
    return TaskResponse(
        content = content,
        priority = priority,
        isCompleted = isCompleted
    )
}
