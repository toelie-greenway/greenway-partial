package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatusDetail
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderUseCase.GetQrOrderResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrOrderStatusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQrOrderUseCase: GetQrOrderUseCase
) : ViewModel() {

    private val _orderId = MutableStateFlow("")

    private val _uiState = MutableStateFlow(QrOrderStatusUiState())
    val uiState = _uiState.asStateFlow()

    private var loadStatusJob: Job? = null

    init {
        viewModelScope.launch {
            _orderId.collect {
                loadData(it)
            }
        }

        val args = QrOrderStatusFragmentArgs.fromSavedStateHandle(savedStateHandle)
        setOrderId(args.orderStatusArg.qrOrderId)
    }

    private fun loadData(orderId: String) {
        if (orderId.isEmpty()) {
            _uiState.update {
                it.copy(order = null)
            }
            return
        }

        loadStatusJob?.cancel()
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            runCancellableCatching {
                getQrOrderUseCase(GetQrOrderUseCase.Param(orderId))
            }.onSuccess { result ->
                when (result) {
                    is GetQrOrderResult.Success -> {
                        _uiState.update {
                            it.copy(
                                order = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is GetQrOrderResult.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = "Arr!!!")
                        }
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(isLoading = false, error = "Arr!!!")
                }
            }
        }

    }

    private fun setOrderId(orderId: String) {
        if (orderId == _orderId.value) {
            return
        }

        _orderId.value = orderId
    }
}

data class QrOrderStatusUiState(
    val isLoading: Boolean = false,
    val order: QrOrder? = null,
    val error: String? = null
) {
    val orderId: String = order?.id.orEmpty()
    val orderIdNumber: String = order?.qrIdNumber.orEmpty()
    val statuses: List<QrOrderStatusItemUiState> = mapToListUiItem(order?.statuses.orEmpty())
    val title: Text
        get() = Text.ResourceFormattedText(
            R.string.label_farming_record_qr_order_status_title,
            listOf(orderIdNumber)
        )

    private fun mapToListUiItem(data: List<QrOrderStatusDetail>): List<QrOrderStatusItemUiState> {
        return if (data.size == 1) {
            listOf(
                QrOrderStatusItemUiState.ListItem(data[0]),
                QrOrderStatusItemUiState.PlaceholderItem,
                QrOrderStatusItemUiState.PlaceholderItem,
                QrOrderStatusItemUiState.PlaceholderItem,
                QrOrderStatusItemUiState.PlaceholderItem,
                QrOrderStatusItemUiState.PlaceholderItem,
            )
        } else {
            data.map {
                QrOrderStatusItemUiState.ListItem(it)
            }
        }
    }
}

sealed class QrOrderStatusItemUiState {
    abstract val id: String

    data class ListItem(val item: QrOrderStatusDetail) : QrOrderStatusItemUiState() {
        override val id: String
            get() = item.id
    }

    object PlaceholderItem : QrOrderStatusItemUiState() {
        override val id: String
            get() = "-1"
    }
}