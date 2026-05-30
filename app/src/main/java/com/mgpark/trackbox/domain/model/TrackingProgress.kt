package com.mgpark.trackbox.domain.model

import java.time.Instant

data class TrackingProgress(
    val time: Instant,
    val state: TrackingState,
    val location: String?,
    val description: String,
)
