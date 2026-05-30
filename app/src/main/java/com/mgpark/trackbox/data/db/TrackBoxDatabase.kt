package com.mgpark.trackbox.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TrackingEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class TrackBoxDatabase : RoomDatabase() {
    abstract fun trackingDao(): TrackingDao

    companion object {
        const val NAME = "trackbox.db"
    }
}
