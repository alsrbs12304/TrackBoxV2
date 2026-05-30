package com.mgpark.trackbox.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.mgpark.trackbox.core.designsystem.theme.DeliveryTheme
import com.mgpark.trackbox.domain.model.TrackingState

val TrackingState.label: String
    get() = when (this) {
        TrackingState.UNKNOWN              -> "상태 확인 중"
        TrackingState.INFORMATION_RECEIVED -> "접수"
        TrackingState.AT_PICKUP            -> "집화 완료"
        TrackingState.IN_TRANSIT           -> "이동 중"
        TrackingState.OUT_FOR_DELIVERY     -> "배송 출발"
        TrackingState.DELIVERED            -> "배송 완료"
        TrackingState.EXCEPTION            -> "배송 예외"
    }

data class TrackingStatePalette(val background: Color, val onBackground: Color)

@Composable
@ReadOnlyComposable
fun TrackingState.palette(): TrackingStatePalette {
    val colors = DeliveryTheme.colors
    return when (this) {
        TrackingState.DELIVERED  -> TrackingStatePalette(colors.delivered, colors.onDelivered)
        TrackingState.EXCEPTION  -> TrackingStatePalette(colors.failure, colors.onFailure)
        TrackingState.UNKNOWN,
        TrackingState.INFORMATION_RECEIVED -> TrackingStatePalette(colors.pending, colors.onPending)
        else -> TrackingStatePalette(colors.inTransit, colors.onInTransit)
    }
}
