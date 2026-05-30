package com.mgpark.trackbox.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mgpark.trackbox.domain.repository.TrackingRepository
import com.mgpark.trackbox.ui.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrackingRepository,
) : ViewModel() {

    private val trackingId: Long = savedStateHandle.toRoute<DetailRoute>().id

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getTrackingDetail(trackingId)
                .onSuccess { d ->
                    _uiState.update { it.copy(isLoading = false, detail = d) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "조회 실패")
                    }
                }
        }
    }

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            repository.refreshTracking(trackingId)
                .onSuccess { d ->
                    _uiState.update { it.copy(isRefreshing = false, detail = d) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isRefreshing = false, errorMessage = e.message ?: "새로고침 실패")
                    }
                }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.removeTracking(trackingId)
            _uiState.update { it.copy(deleted = true) }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
