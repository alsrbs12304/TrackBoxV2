package com.mgpark.trackbox.ui.detail

import com.mgpark.trackbox.domain.model.TrackingDetail

data class DetailUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val detail: TrackingDetail? = null,
    val errorMessage: String? = null,
    val deleted: Boolean = false,
)
