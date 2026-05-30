package com.mgpark.trackbox.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mgpark.trackbox.domain.model.CarrierId
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingState
import java.time.Instant

@Entity(
    tableName = "tracking",
    indices = [
        Index(value = ["carrierRaw", "trackingNumber"], unique = true),
    ],
)
data class TrackingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val carrierRaw: String,
    val trackingNumber: String,
    val alias: String?,
    val state: TrackingState,
    val lastDescription: String?,
    val lastLocation: String?,
    val lastEventAt: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isArchived: Boolean,
)

fun TrackingEntity.toDomain(): Tracking = Tracking(
    id = id,
    trackingNumber = trackingNumber,
    carrierId = CarrierId.fromRaw(carrierRaw) ?: error("Unknown carrier raw: $carrierRaw"),
    alias = alias,
    state = state,
    lastDescription = lastDescription,
    lastLocation = lastLocation,
    lastEventAt = lastEventAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isArchived = isArchived,
)
