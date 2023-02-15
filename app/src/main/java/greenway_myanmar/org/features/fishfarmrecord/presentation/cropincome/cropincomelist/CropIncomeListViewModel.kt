package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.cropincomelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCropIncomesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCropIncomesStreamUseCase.GetCropIncomesRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.toJavaInstant
import javax.inject.Inject

@HiltViewModel
class CropIncomeListViewModel @Inject constructor(
    getCropIncomesStreamUseCase: GetCropIncomesStreamUseCase
) : ViewModel() {

    private val farmUiState = MutableStateFlow<FarmUiState>(LoadingState.Idle)

    val cropIncomesUiState: StateFlow<LoadingState<List<CropIncomeListUiState>>> =
        farmUiState.flatMapLatest { farmUiState ->
            cropIncomesStream(farmUiState, getCropIncomesStreamUseCase)
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    fun handleEvent(event: CropIncomeListEvent) {
        when (event) {
            is CropIncomeListEvent.OnFarmChanged -> {
                updateFarm(event.farmUiState)
            }
        }
    }

    private fun updateFarm(farmUiState: LoadingState<UiFarm>) {
        this.farmUiState.value = farmUiState
    }
}

private fun cropIncomesStream(
    farmUiState: FarmUiState,
    getCropIncomesStreamUseCase: GetCropIncomesStreamUseCase
): Flow<LoadingState<List<CropIncomeListUiState>>> {
    return flow {
        emit(LoadingState.Loading)
        when (farmUiState) {
            is LoadingState.Success -> {
                val openingSeason = farmUiState.data.ongoingSeason
                if (openingSeason == null) {
                    emit(LoadingState.Empty(Text.ResourceText(R.string.ffr_farm_detail_label_no_ongoing_season)))
                } else {
                    emitAll(
                        loadCropIncomesStream(openingSeason.id, getCropIncomesStreamUseCase)
                    )
                }
            }
            LoadingState.Loading -> {
                emit(LoadingState.Loading)
            }
            else -> {
                emit(LoadingState.Idle)
            }
        }
    }
}

private fun loadCropIncomesStream(
    seasonId: String,
    getCropIncomesStreamUseCase: GetCropIncomesStreamUseCase
) = getCropIncomesStreamUseCase(
    GetCropIncomesRequest(seasonId)
)
    .catch {
        LoadingState.Empty(it.errorText())
    }.map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception)
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                LoadingState.Success(result.data.map {
                    CropIncomeListUiState(
                        id = it.id,
                        cropName = it.crop.name,
                        income = it.income,
                        date = it.date.toJavaInstant()
                    )
                })
            }
        }
    }