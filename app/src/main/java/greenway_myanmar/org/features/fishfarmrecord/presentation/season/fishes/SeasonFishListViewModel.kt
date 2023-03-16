package greenway_myanmar.org.features.fishfarmrecord.presentation.season.fishes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SeasonFishListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeasonFishListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(fishes = SeasonFishListFragmentArgs.fromSavedStateHandle(savedStateHandle).fishes.toList())
        }
    }
}