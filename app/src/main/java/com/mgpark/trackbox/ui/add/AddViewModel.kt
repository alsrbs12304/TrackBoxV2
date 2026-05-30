package com.mgpark.trackbox.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgpark.trackbox.domain.model.CarrierId
import com.mgpark.trackbox.domain.repository.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TrackingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddUiState())
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    fun selectCarrier(carrier: CarrierId) {
        _uiState.update { it.copy(carrier = carrier) }
    }

    fun onTrackingNumberChange(value: String) {
        val digitsOnly = value.filter(Char::isDigit).take(20)
        _uiState.update { it.copy(trackingNumber = digitsOnly) }
    }

    fun onAliasChange(value: String) {
        _uiState.update { it.copy(alias = value) }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun submit() {
        val s = _uiState.value
        val carrier = s.carrier ?: return
        if (s.trackingNumber.isBlank() || s.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            repository.addTracking(carrier, s.trackingNumber, s.alias.ifBlank { null })
                .onSuccess { tracking ->
                    _uiState.update { it.copy(isSubmitting = false, submittedId = tracking.id) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSubmitting = false, errorMessage = e.message ?: "추가 실패")
                    }
                }
        }
    }
}
