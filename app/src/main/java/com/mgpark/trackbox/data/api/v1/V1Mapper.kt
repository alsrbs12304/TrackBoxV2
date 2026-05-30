package com.mgpark.trackbox.data.api.v1

import com.mgpark.trackbox.domain.model.TrackingParty
import com.mgpark.trackbox.domain.model.TrackingProgress
import com.mgpark.trackbox.domain.model.TrackingState
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

internal fun String?.toInstantOrNull(): Instant? {
    if (this.isNullOrBlank()) return null
    return runCatching { OffsetDateTime.parse(this).toInstant() }
        .recoverCatching { Instant.parse(this) }
        .getOrElse {
            if (it is DateTimeParseException) null else throw it
        }
}

internal fun StateDto?.toTrackingState(): TrackingState = when (this?.id?.lowercase()) {
    "information_received" -> TrackingState.INFORMATION_RECEIVED
    "at_pickup"            -> TrackingState.AT_PICKUP
    "in_transit"           -> TrackingState.IN_TRANSIT
    "out_for_delivery"     -> TrackingState.OUT_FOR_DELIVERY
    "delivered"            -> TrackingState.DELIVERED
    "exception"            -> TrackingState.EXCEPTION
    else                   -> TrackingState.UNKNOWN
}

internal fun PartyDto?.toDomain(): TrackingParty? {
    if (this == null) return null
    if (name.isNullOrBlank()) return null
    return TrackingParty(name = name, location = null)
}

internal fun ProgressDto.toDomain(): TrackingProgress? {
    val instant = time.toInstantOrNull() ?: return null
    return TrackingProgress(
        time = instant,
        state = status.toTrackingState(),
        location = location?.name?.takeIf { it.isNotBlank() },
        description = description.orEmpty(),
    )
}
