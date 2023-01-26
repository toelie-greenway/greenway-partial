package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import java.time.LocalDate

sealed class AddEditExpenseEvent {
    data class OnDateChanged(val date: LocalDate): AddEditExpenseEvent()
    data class OnCategoryChanged(val category: UiExpenseCategory): AddEditExpenseEvent()
    data class OnLabourCostChanged(val labourCost: UiLabourCost): AddEditExpenseEvent()
    data class OnMachineryCostChanged(val machineryCost: UiMachineryCost): AddEditExpenseEvent()
    data class OnFarmInputAdded(val farmInput: UiFarmInputCost): AddEditExpenseEvent()
    data class OnNoteChanged(val note: String): AddEditExpenseEvent()
}
