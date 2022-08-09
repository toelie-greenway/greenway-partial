package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmingRecordQrHomeViewModel @Inject constructor(
    private val getQrOrderListUseCase: GetQrOrderListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmingRecordQrHomeUiState())
    val uiState = _uiState.asStateFlow()

    private var loadQrListJob: Job? = null

    init {
        loadQrList()
    }

    private fun loadQrList() {
        loadQrListJob?.cancel()

        loadQrListJob = viewModelScope.launch {
            getQrOrderListUseCase()
                .collect { result ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(qrOrderList = result.map { UiQrOrder.fromDomain(it) })
                    }
                }
        }
    }
}


data class FarmingRecordQrHomeUiState(
    val qrOrderList: List<UiQrOrder> = emptyList()
)