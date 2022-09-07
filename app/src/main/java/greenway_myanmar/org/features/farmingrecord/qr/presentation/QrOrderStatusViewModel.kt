package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatusDetail
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderStatusUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrOrderStatusUseCase.GetQrOrderStatusUseCaseResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrOrderStatusViewModel @Inject constructor(
    private val getQrOrderStatusUseCase: GetQrOrderStatusUseCase
) : ViewModel() {

    private val _orderId = MutableStateFlow<String>("")

    private val _uiState = MutableStateFlow(QrOrderStatusUiState())
    val uiState = _uiState.asStateFlow()

    private var loadStatusJob: Job? = null

    init {
        viewModelScope.launch {
            _orderId.collect {
                loadData(it)
            }
        }
    }

    private fun loadData(orderId: String) {
        if (orderId.isEmpty()) {
            _uiState.update {
                it.copy(statuses = emptyList())
            }
            return
        }

        loadStatusJob?.cancel()
        viewModelScope.launch {
            runCancellableCatching {
                getQrOrderStatusUseCase()
            }.onSuccess { result ->
                when (result) {
                    GetQrOrderStatusUseCaseResult.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is GetQrOrderStatusUseCaseResult.Success -> {
                        _uiState.update {
                            it.copy(
                                statuses = mapToListUiItem(result.data),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is GetQrOrderStatusUseCaseResult.Error -> {
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

    fun setOrderId(orderId: String) {
        if (orderId == _orderId.value) {
            return
        }

        _orderId.value = orderId
    }
}

data class QrOrderStatusUiState(
    val isLoading: Boolean = false,
    val statuses: List<QrOrderStatusItemUiState> = emptyList(),
    val error: String? = null
)

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