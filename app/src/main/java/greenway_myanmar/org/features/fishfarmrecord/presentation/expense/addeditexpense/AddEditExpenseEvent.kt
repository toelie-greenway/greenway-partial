package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import java.time.LocalDate

sealed class AddEditExpenseEvent {
    data class OnDateChanged(val date: LocalDate): AddEditExpenseEvent()
    data class OnCategoryChanged(val category: UiExpenseCategory): AddEditExpenseEvent()
    data class OnGeneralExpenseCategorySelectionChanged(val position: Int) : AddEditExpenseEvent()
    data class OnLabourCostChanged(val labourCost: UiLabourCost): AddEditExpenseEvent()
    data class OnMachineryCostChanged(val machineryCost: UiMachineryCost): AddEditExpenseEvent()
    data class OnFarmInputAdded(val farmInput: UiFarmInputCost): AddEditExpenseEvent()
    data class OnFarmInputRemoved(val farmInput: UiFarmInputCost) : AddEditExpenseEvent()
    data class OnGeneralExpenseChanged(val expense: String): AddEditExpenseEvent()
    data class OnNoteChanged(val note: String): AddEditExpenseEvent()
    object OnSubmit: AddEditExpenseEvent()
    object CostErrorShown : AddEditExpenseEvent()
    object OnExpenseUploadingErrorShown : AddEditExpenseEvent()
}
