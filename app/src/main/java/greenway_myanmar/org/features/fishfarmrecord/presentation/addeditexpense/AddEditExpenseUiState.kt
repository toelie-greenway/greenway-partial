package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import java.time.LocalDate

data class AddEditExpenseUiState(
    val date: LocalDate = LocalDate.now(),
    val category: UiExpenseCategory? = null,
    val labourCost: UiLabourCost? = null,
    val machineryCost: UiMachineryCost? = null,
    val farmInputs: List<UiFarmInputCost> = emptyList(),
    val note: String? = null,
)
