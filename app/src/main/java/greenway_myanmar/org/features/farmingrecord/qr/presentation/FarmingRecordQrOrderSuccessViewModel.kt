package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FarmingRecordQrOrderSuccessViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args =
        FarmingRecordQrOrderSuccessFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _uiState = MutableStateFlow(FarmingRecordQrOrderSuccess())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(quantity = args.quantity)
        }
    }

}

data class FarmingRecordQrOrderSuccess(
    val quantity: Int = 0
) {
    val message: Text = Text.ResourceFormattedText(
        R.string.label_farming_record_qr_order_success_message,
        listOf(quantity)
    )
}