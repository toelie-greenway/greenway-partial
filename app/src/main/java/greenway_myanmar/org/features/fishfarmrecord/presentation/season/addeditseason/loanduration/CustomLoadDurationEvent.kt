package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.loanduration

sealed class CustomLoadDurationEvent {
    data class OnMonthChanged(val month: String?) : CustomLoadDurationEvent()
    object OnSubmit : CustomLoadDurationEvent()
}
