package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import java.time.LocalDate

sealed class AddEditExpenseEvent {
    data class OnDateChanged(val date: LocalDate): AddEditExpenseEvent()
    data class OnCategoryChanged(val category: UiExpenseCategory): AddEditExpenseEvent()
}
