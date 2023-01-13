package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput.FishInputUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddEditSeasonViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSeasonUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditSeasonUiState
        get() = uiState.value

    fun handleEvent(event: AddEditSeasonUiEvent) {
        when (event) {
            is AddEditSeasonUiEvent.OnFishListChanged -> {
                updateFishList(event.fishes)
            }
            is AddEditSeasonUiEvent.OnFishAdded -> {
                addFish(event.fish)
            }
        }
    }

    private fun addFish(fish: UiFish) {
        val newFishList = mutableListOf<UiFish>()
        newFishList.addAll(currentUiState.fishes)
        newFishList.add(fish)
        updateFishList(newFishList)
    }

    private fun updateFishList(fishes: List<UiFish>) {
        if (currentUiState.fishes == fishes) {
            return
        }

        _uiState.value = currentUiState.copy(fishes = fishes)
    }
}