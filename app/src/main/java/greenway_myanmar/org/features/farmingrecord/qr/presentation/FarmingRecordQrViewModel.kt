package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FarmingRecordQrActivityViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmingRecordQrUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: FarmingRecordQrEvent) {
        when (event) {
            FarmingRecordQrEvent.Refresh -> {
                updateRefresh(true)
            }
            FarmingRecordQrEvent.RefreshHandled -> {
                updateRefresh(false)
            }
        }
    }

    private fun updateRefresh(refresh: Boolean) {
        _uiState.update {
            it.copy(refreshPending = refresh)
        }
    }

}

sealed class FarmingRecordQrEvent {
    object Refresh : FarmingRecordQrEvent()
    object RefreshHandled : FarmingRecordQrEvent()
}

data class FarmingRecordQrUiState(
    val refreshPending: Boolean = false
)