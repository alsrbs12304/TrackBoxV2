package com.mgpark.trackbox.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {

    @Query("SELECT * FROM tracking ORDER BY isArchived ASC, updatedAt DESC")
    fun observeAll(): Flow<List<TrackingEntity>>

    @Query("SELECT * FROM tracking WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TrackingEntity?

    @Query("SELECT * FROM tracking WHERE carrierRaw = :carrierRaw AND trackingNumber = :trackingNumber LIMIT 1")
    suspend fun findByKey(carrierRaw: String, trackingNumber: String): TrackingEntity?

    @Query("SELECT id FROM tracking WHERE isArchived = 0")
    suspend fun getActiveIds(): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: TrackingEntity): Long

    @Update
    suspend fun update(entity: TrackingEntity)

    @Query("UPDATE tracking SET isArchived = :archived, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean, updatedAt: Long)

    @Query("DELETE FROM tracking WHERE id = :id")
    suspend fun deleteById(id: Long)
}
