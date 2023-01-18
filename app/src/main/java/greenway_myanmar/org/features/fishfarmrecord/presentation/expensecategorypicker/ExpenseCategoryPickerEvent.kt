package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

sealed class ExpenseCategoryPickerEvent {
    data class ToggleCategorySelection(val categoryId: String) : ExpenseCategoryPickerEvent()
}