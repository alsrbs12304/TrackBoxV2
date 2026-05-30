package com.mgpark.trackbox.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class DeliveryColors(
    val delivered: Color,
    val inTransit: Color,
    val pending: Color,
    val failure: Color,
    val onDelivered: Color,
    val onInTransit: Color,
    val onPending: Color,
    val onFailure: Color,
)

internal val LightDeliveryColors = DeliveryColors(
    delivered = Emerald500,
    inTransit = Amber500,
    pending = Slate400,
    failure = Rose500,
    onDelivered = Color.White,
    onInTransit = OnIntransitLight,
    onPending = Color.White,
    onFailure = Color.White,
)

internal val DarkDeliveryColors = DeliveryColors(
    delivered = Emerald400,
    inTransit = Amber400,
    pending = Slate500,
    failure = Rose400,
    onDelivered = OnDeliveredDark,
    onInTransit = OnIntransitDark,
    onPending = Slate50,
    onFailure = OnFailureDark,
)

internal val LocalDeliveryColors = compositionLocalOf { LightDeliveryColors }

object DeliveryTheme {
    val colors: DeliveryColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDeliveryColors.current

    @Suppress("unused")
    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}
