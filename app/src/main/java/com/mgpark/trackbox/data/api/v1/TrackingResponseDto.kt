package com.mgpark.trackbox.data.api.v1

import kotlinx.serialization.Serializable

@Serializable
data class TrackingResponseDto(
    val carrier: CarrierDto? = null,
    val from: PartyDto? = null,
    val to: PartyDto? = null,
    val state: StateDto? = null,
    val progresses: List<ProgressDto> = emptyList(),
)

@Serializable
data class CarrierDto(
    val id: String? = null,
    val name: String? = null,
    val tel: String? = null,
)

@Serializable
data class PartyDto(
    val name: String? = null,
    val time: String? = null,
)

@Serializable
data class StateDto(
    val id: String? = null,
    val text: String? = null,
)

@Serializable
data class ProgressDto(
    val time: String? = null,
    val location: LocationDto? = null,
    val status: StateDto? = null,
    val description: String? = null,
)

@Serializable
data class LocationDto(
    val name: String? = null,
)
