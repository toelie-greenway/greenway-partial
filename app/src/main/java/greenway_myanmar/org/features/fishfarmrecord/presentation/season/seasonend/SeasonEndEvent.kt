package greenway_myanmar.org.features.fishfarmrecord.presentation.season.seasonend

sealed class SeasonEndEvent {
    data class OnReasonChanged(val item: SeasonEndReasonListItemUiState) : SeasonEndEvent()
    object OnSubmit : SeasonEndEvent()
    object OnReasonErrorShown : SeasonEndEvent()
}
