package com.mgpark.trackbox

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.mgpark.trackbox.notification.DeliveryRefreshScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TrackBoxApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        DeliveryRefreshScheduler.schedule(this)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = getSystemService(NotificationManager::class.java) ?: return
        val deliveryUpdates = NotificationChannel(
            CHANNEL_DELIVERY_UPDATES,
            getString(R.string.notification_channel_delivery_updates),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = getString(R.string.notification_channel_delivery_updates_desc)
        }
        manager.createNotificationChannel(deliveryUpdates)
    }

    companion object {
        const val CHANNEL_DELIVERY_UPDATES = "delivery_updates"
    }
}
