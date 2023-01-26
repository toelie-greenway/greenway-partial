package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.ObservePondsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FishFarmerRecordBookHomeViewModel @Inject constructor(
    private val observePondsUseCase: ObservePondsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(FishFarmerRecordBookHomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPonds()
    }

    private fun loadPonds() {
        viewModelScope.launch {
            observePondsUseCase(Unit).collect { result ->
                when (result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                ponds = result.data.map { pond ->
                                    FarmListItemUiState.fromDomain(pond)
                                })
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = result.exception.errorText())
                        }
                    }
                }
            }
        }
    }
}