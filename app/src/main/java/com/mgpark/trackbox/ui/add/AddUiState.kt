package com.mgpark.trackbox.ui.add

import com.mgpark.trackbox.domain.model.CarrierId

data class AddUiState(
    val carrier: CarrierId? = null,
    val trackingNumber: String = "",
    val alias: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val submittedId: Long? = null,
) {
    val canSubmit: Boolean
        get() = !isSubmitting && carrier != null && trackingNumber.isNotBlank()
}
