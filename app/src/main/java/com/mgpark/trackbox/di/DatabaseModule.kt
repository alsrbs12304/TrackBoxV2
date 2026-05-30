package com.mgpark.trackbox.di

import android.content.Context
import androidx.room.Room
import com.mgpark.trackbox.data.db.TrackBoxDatabase
import com.mgpark.trackbox.data.db.TrackingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrackBoxDatabase =
        Room.databaseBuilder(context, TrackBoxDatabase::class.java, TrackBoxDatabase.NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTrackingDao(db: TrackBoxDatabase): TrackingDao = db.trackingDao()
}
