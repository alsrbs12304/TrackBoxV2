package com.mgpark.trackbox.di

import com.mgpark.trackbox.data.repository.TrackingRepositoryImpl
import com.mgpark.trackbox.domain.repository.TrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTrackingRepository(impl: TrackingRepositoryImpl): TrackingRepository
}
