package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import java.time.LocalDate

data class AddEditExpenseUiState(
    val date: LocalDate = LocalDate.now(),
    val category: UiExpenseCategory? = null,
    val categoryError: Text? = null,
    val labourCost: UiLabourCost? = null,
    val machineryCost: UiMachineryCost? = null,
    val farmInputCosts: List<UiFarmInputCost> = emptyList(),
    val costError: Text? = null,

    val note: String? = null,

    val addEditExpenseResult: AddEditExpenseResult? = null,
) {
    data class AddEditExpenseResult(
        val expenseId: String
    )
}
