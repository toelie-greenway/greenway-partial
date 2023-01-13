package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.common.result.mapList
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.UserFishRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFishesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FishPickerViewModel @Inject constructor(
    getFishesStreamUseCase: GetFishesStreamUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")

    private val _selectedFishIdsStream = MutableStateFlow<Set<String>>(emptySet())
    private val currentSelectedFishIds: Set<String>
        get() = _selectedFishIdsStream.value

    val fishesUiState: StateFlow<FishesUiState> = _query.flatMapLatest { query ->
        fishesUiStateStream(
            query,
            getFishesStreamUseCase,
            _selectedFishIdsStream
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = FishesUiState.Loading
        )

//
//    val userCrops: LiveData<Resource<List<UiFish>>> =
//        _starterEvent.switchMap { userCropRepository.userCrops }
//
//    fun start() {
//        _starterEvent.call()
//        _keyword.value = ""
//    }

    fun setQuery(query: String) {
        _query.value = query
    }

    fun handleEvent(event: FishPickerEvent) {
        when (event) {
            is FishPickerEvent.ToggleFishSelection -> {
                toggleFishSelection(event.fishId)
            }
        }
    }

    private fun toggleFishSelection(fishId: String) {
        val updatedSelectedFishIds = currentSelectedFishIds.toMutableSet()
        if (currentSelectedFishIds.contains(fishId)) {
            updatedSelectedFishIds.removeIf { it == fishId }
        } else {
            updatedSelectedFishIds.add(fishId)
        }
        _selectedFishIdsStream.value = updatedSelectedFishIds
    }
}

private fun fishesUiStateStream(
    query: String,
    getFishesStreamUseCase: GetFishesStreamUseCase,
    selectedFishIdsStream: Flow<Set<String>>
): Flow<FishesUiState> {
    return combine(
        getFishesStreamUseCase(query),
        selectedFishIdsStream,
        ::Pair
    ).asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    val (fishes, selectedFishIds) = result.data
                    FishesUiState.Success(fishes.map { fish ->
                        FishPickerListItemUiState(
                            UiFish.fromDomain(fish),
                            selectedFishIds.contains(fish.id)
                        )
                    })
                }
                is Result.Error -> {
                    FishesUiState.Error
                }
                Result.Loading -> {
                    FishesUiState.Loading
                }
            }
        }
}


sealed interface FishesUiState {
    data class Success(val data: List<FishPickerListItemUiState>) : FishesUiState
    object Error : FishesUiState
    object Loading : FishesUiState
}