package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish

data class ExpenseCategoryPickerListItemUiState(
    val category: UiExpenseCategory,
    val checked: Boolean
)