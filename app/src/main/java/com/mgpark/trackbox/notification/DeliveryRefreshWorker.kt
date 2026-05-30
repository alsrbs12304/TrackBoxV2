package com.mgpark.trackbox.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingState
import com.mgpark.trackbox.domain.repository.TrackingRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class DeliveryRefreshWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: TrackingRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val before = repository.observeAll().first().associate { it.id to it.state }
        val refreshed = repository.refreshAll()
        if (refreshed.isFailure) return Result.retry()

        val after = repository.observeAll().first()
        val transitions = after.filter { tracking ->
            val prev = before[tracking.id] ?: return@filter false
            prev != tracking.state && tracking.state != TrackingState.UNKNOWN
        }
        if (transitions.isNotEmpty()) postNotifications(transitions)
        return Result.success()
    }

    private fun postNotifications(transitions: List<Tracking>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val manager = NotificationManagerCompat.from(context)
        transitions.forEach { tracking ->
            manager.notify(tracking.id.toInt(), DeliveryNotifications.build(context, tracking))
        }
    }
}
