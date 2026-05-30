package com.mgpark.trackbox.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mgpark.trackbox.MainActivity
import com.mgpark.trackbox.R
import com.mgpark.trackbox.TrackBoxApp
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingState

object DeliveryNotifications {

    fun build(context: Context, tracking: Tracking): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(
            context,
            tracking.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val title = tracking.alias?.takeIf { it.isNotBlank() } ?: tracking.carrierId.displayName
        val state = tracking.state.koreanLabel()
        val text = tracking.lastDescription?.takeIf { it.isNotBlank() } ?: state

        return NotificationCompat.Builder(context, TrackBoxApp.CHANNEL_DELIVERY_UPDATES)
            .setSmallIcon(R.drawable.ic_stat_delivery)
            .setContentTitle("$title · $state")
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun TrackingState.koreanLabel(): String = when (this) {
        TrackingState.UNKNOWN              -> "상태 확인 중"
        TrackingState.INFORMATION_RECEIVED -> "접수"
        TrackingState.AT_PICKUP            -> "집화 완료"
        TrackingState.IN_TRANSIT           -> "이동 중"
        TrackingState.OUT_FOR_DELIVERY     -> "배송 출발"
        TrackingState.DELIVERED            -> "배송 완료"
        TrackingState.EXCEPTION            -> "배송 예외"
    }
}
