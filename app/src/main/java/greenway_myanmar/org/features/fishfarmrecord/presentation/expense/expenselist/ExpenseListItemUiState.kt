package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist

import java.math.BigDecimal

//class ExpenseListItemUiState {
//}

data class ExpenseLineItemUiState(
    val name: String,
    val quantity: String,
    val cost: BigDecimal
)
