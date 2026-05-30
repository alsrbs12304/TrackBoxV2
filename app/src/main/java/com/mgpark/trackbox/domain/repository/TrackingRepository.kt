package com.mgpark.trackbox.domain.repository

import com.mgpark.trackbox.domain.model.CarrierId
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingDetail
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {

    fun observeAll(): Flow<List<Tracking>>

    suspend fun getById(id: Long): Tracking?

    suspend fun addTracking(
        carrierId: CarrierId,
        trackingNumber: String,
        alias: String?,
    ): Result<Tracking>

    suspend fun removeTracking(id: Long)

    suspend fun setArchived(id: Long, archived: Boolean)

    suspend fun getTrackingDetail(id: Long): Result<TrackingDetail>

    suspend fun refreshTracking(id: Long): Result<TrackingDetail>

    suspend fun refreshAll(): Result<Int>
}
