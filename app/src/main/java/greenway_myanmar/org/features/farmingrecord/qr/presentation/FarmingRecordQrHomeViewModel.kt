package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.*
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

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private var loadQrListJob: Job? = null

    init {
        viewModelScope.launch {
            loadDataSignal.collectLatest {
                loadQrList()
            }
        }
    }

    fun handleEvent(event: FarmingRecordQrHomeEvent) {
        when (event) {
            is FarmingRecordQrHomeEvent.Retry -> {
                retry()
            }
        }
    }

    private fun retry() {
        loadQrList()
    }

    private fun loadQrList() {
        viewModelScope.launch {
            loadQrListJob?.cancel()
            loadQrListJob = launch {
                _uiState.update {
                    it.copy(qrOrderListError = null, qrOrderListLoading = true)
                }
                when (val result = getQrOrderListUseCase()) {
                    is GetQrOrderListResult.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                qrOrderList = result.data.map {
                                    UiQrOrder.fromDomain(
                                        it
                                    )
                                },
                                qrOrderListLoading = false
                            )
                        }
                    }
                    is GetQrOrderListResult.Error -> {
                        _uiState.update {
                            it.copy(
                                qrOrderListError = result.error.error,
                                qrOrderListLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class FarmingRecordQrHomeEvent {
    object Retry : FarmingRecordQrHomeEvent()
}

data class FarmingRecordQrHomeUiState(
    val qrOrderList: List<UiQrOrder> = emptyList(),
    val qrOrderListLoading: Boolean = false,
    val qrOrderListError: Text? = null
)