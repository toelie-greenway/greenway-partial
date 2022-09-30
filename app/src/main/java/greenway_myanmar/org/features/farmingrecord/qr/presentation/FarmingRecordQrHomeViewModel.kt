package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderListUseCase.*
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
                loadQrList(load = true)
            }
        }
    }

    fun handleEvent(event: FarmingRecordQrHomeEvent) {
        when (event) {
            FarmingRecordQrHomeEvent.Retry -> {
                retry()
            }
            FarmingRecordQrHomeEvent.Refresh -> {
                refresh()
            }
            FarmingRecordQrHomeEvent.ScrollToTopHandled -> {
                updateScrollToTop(false)
            }
        }
    }

    private fun updateScrollToTop(scroll: Boolean) {
        _uiState.update {
            it.copy(scrollToTopPending = scroll)
        }
    }

    private fun retry() {
        loadQrList(load = true)
    }

    private fun refresh() {
        val isLoading = uiState.value.qrOrderListLoading
        val isRefreshing = uiState.value.qrOrderListRefreshing
        if (isLoading || isRefreshing) {
            return
        }

        loadQrList(refresh = true)
    }

    private fun loadQrList(load: Boolean = false, refresh: Boolean = false) {
        viewModelScope.launch {
            loadQrListJob?.cancel()
            loadQrListJob = launch {
                _uiState.update {
                    it.copy(
                        qrOrderListError = null,
                        qrOrderListLoading = load,
                        qrOrderListRefreshing = refresh
                    )
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
                                qrOrderListLoading = false,
                                qrOrderListRefreshing = false,
                            )
                        }
                        delay(300)
                        _uiState.update {
                            it.copy(scrollToTopPending = refresh)
                        }
                    }
                    is GetQrOrderListResult.Error -> {
                        _uiState.update {
                            it.copy(
                                qrOrderListError = result.error.error,
                                qrOrderListLoading = false,
                                qrOrderListRefreshing = false
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
    object Refresh : FarmingRecordQrHomeEvent()
    object ScrollToTopHandled : FarmingRecordQrHomeEvent()
}

data class FarmingRecordQrHomeUiState(
    val qrOrderList: List<UiQrOrder> = emptyList(),
    val qrOrderListLoading: Boolean = false,
    val qrOrderListRefreshing: Boolean = false,
    val qrOrderListError: Text? = null,
    val scrollToTopPending: Boolean = false
)