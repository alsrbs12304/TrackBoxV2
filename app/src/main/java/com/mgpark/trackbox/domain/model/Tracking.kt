package com.mgpark.trackbox.domain.model

import java.time.Instant

data class Tracking(
    val id: Long,
    val trackingNumber: String,
    val carrierId: CarrierId,
    val alias: String?,
    val state: TrackingState,
    val lastDescription: String?,
    val lastLocation: String?,
    val lastEventAt: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isArchived: Boolean,
)
