package com.mgpark.trackbox.data.api

import com.mgpark.trackbox.data.api.v1.TrackingResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackerV1Service {

    @GET("carriers/{carrierId}/tracks/{trackingNumber}")
    suspend fun fetchTracking(
        @Path("carrierId") carrierId: String,
        @Path("trackingNumber") trackingNumber: String,
    ): TrackingResponseDto

    companion object {
        const val BASE_URL = "https://apis.tracker.delivery/"
    }
}
