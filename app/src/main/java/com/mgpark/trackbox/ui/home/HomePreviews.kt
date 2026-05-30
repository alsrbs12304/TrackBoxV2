package com.mgpark.trackbox.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mgpark.trackbox.core.designsystem.theme.TrackBoxTheme
import com.mgpark.trackbox.ui.common.SamplePreviewData

@Preview(name = "Card · Light", showBackground = true)
@Composable
private fun TrackingCardPreview() {
    TrackBoxTheme {
        TrackingCard(
            tracking = SamplePreviewData.inTransit,
            onClick = {},
        )
    }
}

@Preview(name = "List · Light", showBackground = true, heightDp = 720)
@Composable
private fun TrackingListPreview() {
    TrackBoxTheme {
        TrackingList(
            trackings = SamplePreviewData.list,
            padding = PaddingValues(0.dp),
            onClick = {},
        )
    }
}

@Preview(name = "Empty", showBackground = true, heightDp = 600)
@Composable
private fun EmptyStatePreview() {
    TrackBoxTheme {
        EmptyState(padding = PaddingValues(0.dp))
    }
}
