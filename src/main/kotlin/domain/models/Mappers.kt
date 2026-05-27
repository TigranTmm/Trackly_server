package com.example.domain.models

import com.example.dto.sphere.SphereResponse

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
