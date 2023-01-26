package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

sealed class ExpenseCategoryPickerEvent {
    object RetryLoadingCategories : ExpenseCategoryPickerEvent()
    data class ToggleCategorySelection(val categoryId: String) : ExpenseCategoryPickerEvent()
}