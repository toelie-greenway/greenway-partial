package greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

sealed class FishInputUiEvent {
    data class OnFishChanged(val fish: UiFish): FishInputUiEvent()
    data class OnSpeciesChanged(val species: String) : FishInputUiEvent()

    object OnSubmit : FishInputUiEvent()
    object ResetFishValidationError : FishInputUiEvent()
}

sealed class FishInputNavigationEvent {
    data class NavigateBackWithResult(val fish: UiFish): FishInputNavigationEvent()
}