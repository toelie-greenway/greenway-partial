package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import java.time.LocalDate

sealed class AddEditExpenseEvent {
    data class OnDateChanged(val date: LocalDate): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    data class OnCategoryChanged(val category: UiExpenseCategory): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    data class OnLabourCostChanged(val labourCost: UiLabourCost): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    data class OnMachineryCostChanged(val machineryCost: UiMachineryCost): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    data class OnFarmInputAdded(val farmInput: UiFarmInputCost): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    data class OnNoteChanged(val note: String): greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    object OnSubmit: greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
    object CostErrorShown : greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.AddEditExpenseEvent()
}
