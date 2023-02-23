package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmsStreamUseCase
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FishFarmerRecordBookHomeViewModel @Inject constructor(
    getFarmsStreamUseCase: GetFarmsStreamUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(FishFarmerRecordBookHomeUiState())
    val uiState = _uiState.asStateFlow()

    val farms = farmsStream(getFarmsStreamUseCase)
        .stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    private fun farmsStream(
        getFarmsStreamUseCase: GetFarmsStreamUseCase
    ): Flow<LoadingState<List<FarmListItemUiState>>> {
        return getFarmsStreamUseCase()
            .map { result ->
                when (result) {
                    is Result.Error -> {
                        LoadingState.Error(result.exception)
                    }
                    Result.Loading -> {
                        LoadingState.Loading
                    }
                    is Result.Success -> {
                        LoadingState.Success(result.data.map { farm ->
                            FarmListItemUiState.fromDomain(farm)
                        })
                    }
                }
            }
    }
}