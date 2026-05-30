package com.mgpark.trackbox.ui.home

import com.mgpark.trackbox.domain.model.Tracking

data class HomeUiState(
    val trackings: List<Tracking> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean get() = trackings.isEmpty() && !isRefreshing
}
