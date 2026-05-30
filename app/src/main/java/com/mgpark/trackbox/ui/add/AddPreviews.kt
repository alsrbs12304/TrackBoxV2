package com.mgpark.trackbox.ui.add

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mgpark.trackbox.core.designsystem.theme.TrackBoxTheme
import com.mgpark.trackbox.domain.model.CarrierId

@Preview(name = "Empty · Light", showBackground = true, heightDp = 640)
@Composable
private fun AddFormEmptyPreview() {
    TrackBoxTheme {
        AddForm(
            state = AddUiState(),
            padding = PaddingValues(0.dp),
            onCarrier = {},
            onTrackingNumber = {},
            onAlias = {},
            onSubmit = {},
        )
    }
}

@Preview(name = "Filled", showBackground = true, heightDp = 640)
@Composable
private fun AddFormFilledPreview() {
    TrackBoxTheme {
        AddForm(
            state = AddUiState(
                carrier = CarrierId.CJ_LOGISTICS,
                trackingNumber = "1234567890",
                alias = "엄마 선물",
            ),
            padding = PaddingValues(0.dp),
            onCarrier = {},
            onTrackingNumber = {},
            onAlias = {},
            onSubmit = {},
        )
    }
}

@Preview(name = "Submitting", showBackground = true, heightDp = 640)
@Composable
private fun AddFormSubmittingPreview() {
    TrackBoxTheme {
        AddForm(
            state = AddUiState(
                carrier = CarrierId.KOREA_POST,
                trackingNumber = "0987654321",
                alias = "",
                isSubmitting = true,
            ),
            padding = PaddingValues(0.dp),
            onCarrier = {},
            onTrackingNumber = {},
            onAlias = {},
            onSubmit = {},
        )
    }
}
