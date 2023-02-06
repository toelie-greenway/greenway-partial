package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

sealed class FarmInputProductPickerEvent {
    data class OnQueryChanged(val query: String) : FarmInputProductPickerEvent()
}