package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.farminput.products

sealed class FarmInputProductPickerEvent {
    data class OnQueryChanged(val query: String) : FarmInputProductPickerEvent()
}