package com.mgpark.trackbox.domain.model

enum class TrackingState {
    UNKNOWN,
    INFORMATION_RECEIVED,
    AT_PICKUP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    EXCEPTION;

    val isTerminal: Boolean get() = this == DELIVERED || this == EXCEPTION
    val isActive: Boolean get() = !isTerminal && this != UNKNOWN
}
