package com.mgpark.trackbox.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mgpark.trackbox.core.designsystem.theme.TrackBoxTheme
import com.mgpark.trackbox.ui.common.SamplePreviewData

@Preview(name = "Detail · Light", showBackground = true, heightDp = 900)
@Composable
private fun DetailContentPreview() {
    TrackBoxTheme {
        DetailContent(
            detail = SamplePreviewData.detail,
            padding = PaddingValues(0.dp),
        )
    }
}

@Preview(name = "Header", showBackground = true)
@Composable
private fun SummaryHeaderPreview() {
    TrackBoxTheme {
        SummaryHeader(tracking = SamplePreviewData.inTransit)
    }
}

@Preview(name = "Timeline · single", showBackground = true)
@Composable
private fun TimelineRowPreview() {
    TrackBoxTheme {
        TimelineRow(
            progress = SamplePreviewData.detail.progresses.first(),
            isFirst = true,
            isLast = false,
        )
    }
}
