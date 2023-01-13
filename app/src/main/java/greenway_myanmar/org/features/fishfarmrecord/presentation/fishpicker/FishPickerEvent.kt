package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker

sealed class FishPickerEvent {
    data class ToggleFishSelection(val fishId: String) : FishPickerEvent()
}