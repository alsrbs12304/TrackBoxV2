package com.mgpark.trackbox.data.repository

import com.mgpark.trackbox.data.api.TrackerV1Service
import com.mgpark.trackbox.data.api.v1.TrackingResponseDto
import com.mgpark.trackbox.data.api.v1.toDomain
import com.mgpark.trackbox.data.api.v1.toInstantOrNull
import com.mgpark.trackbox.data.api.v1.toTrackingState
import com.mgpark.trackbox.data.db.TrackingDao
import com.mgpark.trackbox.data.db.TrackingEntity
import com.mgpark.trackbox.data.db.toDomain
import com.mgpark.trackbox.domain.model.CarrierId
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingDetail
import com.mgpark.trackbox.domain.model.TrackingState
import com.mgpark.trackbox.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingRepositoryImpl @Inject constructor(
    private val dao: TrackingDao,
    private val service: TrackerV1Service,
) : TrackingRepository {

    override fun observeAll(): Flow<List<Tracking>> =
        dao.observeAll().map { list -> list.map(TrackingEntity::toDomain) }

    override suspend fun getById(id: Long): Tracking? =
        dao.getById(id)?.toDomain()

    override suspend fun addTracking(
        carrierId: CarrierId,
        trackingNumber: String,
        alias: String?,
    ): Result<Tracking> {
        val trimmed = trackingNumber.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("운송장 번호가 비어 있습니다."))
        }
        if (dao.findByKey(carrierId.raw, trimmed) != null) {
            return Result.failure(IllegalStateException("이미 추가된 운송장입니다."))
        }

        // 404 = 해당 운송장이 캐리어 시스템에 존재하지 않음. 등록 거부.
        // 네트워크 오류/5xx/타임아웃은 추가 허용 (오프라인 등록 + 워커가 추후 갱신).
        val remote = runCatching { service.fetchTracking(carrierId.raw, trimmed) }
        if ((remote.exceptionOrNull() as? HttpException)?.code() == 404) {
            return Result.failure(IllegalArgumentException("등록되지 않은 운송장 번호입니다."))
        }

        val now = Instant.now()
        val seed = TrackingEntity(
            carrierRaw = carrierId.raw,
            trackingNumber = trimmed,
            alias = alias?.takeUnless { it.isBlank() },
            state = TrackingState.UNKNOWN,
            lastDescription = null,
            lastLocation = null,
            lastEventAt = null,
            createdAt = now,
            updatedAt = now,
            isArchived = false,
        )
        val id = dao.insert(seed)
        val merged = seed.copy(id = id).applyRemote(remote.getOrNull(), now)
        if (merged != seed.copy(id = id)) dao.update(merged)

        return Result.success(merged.toDomain())
    }

    override suspend fun removeTracking(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun setArchived(id: Long, archived: Boolean) {
        dao.setArchived(id, archived, Instant.now().toEpochMilli())
    }

    override suspend fun getTrackingDetail(id: Long): Result<TrackingDetail> {
        val entity = dao.getById(id)
            ?: return Result.failure(NoSuchElementException("Tracking $id not found"))
        val response = runCatching { service.fetchTracking(entity.carrierRaw, entity.trackingNumber) }
            .getOrElse { return Result.failure(it) }
        return Result.success(buildDetail(entity, response))
    }

    override suspend fun refreshTracking(id: Long): Result<TrackingDetail> {
        val entity = dao.getById(id)
            ?: return Result.failure(NoSuchElementException("Tracking $id not found"))
        val response = runCatching { service.fetchTracking(entity.carrierRaw, entity.trackingNumber) }
            .getOrElse { return Result.failure(it) }

        val updated = entity.applyRemote(response, Instant.now())
        if (updated != entity) dao.update(updated)
        return Result.success(buildDetail(updated, response))
    }

    override suspend fun refreshAll(): Result<Int> = runCatching {
        var changed = 0
        for (id in dao.getActiveIds()) {
            val entity = dao.getById(id) ?: continue
            val response = runCatching {
                service.fetchTracking(entity.carrierRaw, entity.trackingNumber)
            }.getOrNull() ?: continue
            val updated = entity.applyRemote(response, Instant.now())
            if (updated != entity) {
                dao.update(updated)
                changed++
            }
        }
        changed
    }

    private fun buildDetail(entity: TrackingEntity, dto: TrackingResponseDto?): TrackingDetail {
        val progresses = dto?.progresses
            ?.mapNotNull { it.toDomain() }
            ?.sortedByDescending { it.time }
            .orEmpty()
        return TrackingDetail(
            summary = entity.toDomain(),
            sender = dto?.from.toDomain(),
            recipient = dto?.to.toDomain(),
            progresses = progresses,
        )
    }

    private fun TrackingEntity.applyRemote(dto: TrackingResponseDto?, now: Instant): TrackingEntity {
        if (dto == null) return this
        val latest = dto.progresses
            .mapNotNull { p -> p.time.toInstantOrNull()?.let { it to p } }
            .maxByOrNull { it.first }
            ?.second
        val newState = dto.state.toTrackingState().takeUnless { it == TrackingState.UNKNOWN }
            ?: latest?.status.toTrackingState()
        return copy(
            state = newState,
            lastDescription = latest?.description?.takeIf { it.isNotBlank() } ?: lastDescription,
            lastLocation = latest?.location?.name?.takeIf { it.isNotBlank() } ?: lastLocation,
            lastEventAt = latest?.time?.toInstantOrNull() ?: lastEventAt,
            updatedAt = now,
        )
    }
}
