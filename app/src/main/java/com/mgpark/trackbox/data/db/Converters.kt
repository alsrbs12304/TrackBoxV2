package com.mgpark.trackbox.data.db

import androidx.room.TypeConverter
import com.mgpark.trackbox.domain.model.TrackingState
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun fromTrackingState(value: TrackingState): String = value.name

    @TypeConverter
    fun toTrackingState(value: String): TrackingState =
        runCatching { TrackingState.valueOf(value) }.getOrDefault(TrackingState.UNKNOWN)
}
