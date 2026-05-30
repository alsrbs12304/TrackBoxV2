package com.mgpark.trackbox.domain.model

data class TrackingDetail(
    val summary: Tracking,
    val sender: TrackingParty?,
    val recipient: TrackingParty?,
    val progresses: List<TrackingProgress>,
)
