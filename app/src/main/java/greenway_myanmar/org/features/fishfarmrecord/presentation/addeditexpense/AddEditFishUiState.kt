package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import java.time.LocalDate

data class AddEditFishUiState(
    val date: LocalDate = LocalDate.now(),
    val category: UiExpenseCategory? = null,
)
