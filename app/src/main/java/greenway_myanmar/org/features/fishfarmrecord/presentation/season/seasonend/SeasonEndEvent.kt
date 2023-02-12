package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

sealed class SeasonEndEvent {
    data class OnFarmIdChanged(val farmId: String) : SeasonEndEvent()
    data class OnSeasonIdChanged(val seasonId: String) : SeasonEndEvent()
    data class OnReasonChanged(val item: SeasonEndReasonListItemUiState) : SeasonEndEvent()
    object OnSubmit : SeasonEndEvent()
    object OnReasonErrorShown : SeasonEndEvent()
    object OnSavingErrorShown : SeasonEndEvent()
}
