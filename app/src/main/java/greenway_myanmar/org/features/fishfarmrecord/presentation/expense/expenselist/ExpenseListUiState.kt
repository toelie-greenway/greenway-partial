package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist

import com.greenwaymyanmar.core.presentation.model.LoadingState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpense
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import java.math.BigDecimal

data class ExpenseListUiState(
    val category: UiExpenseCategory? = null,
    val total: BigDecimal? = null,
    val expenses: List<UiExpense> = emptyList(),
    val loadingState: LoadingState<Unit> = LoadingState.Idle
) {

    fun success(data: UiCategoryExpense) = ExpenseListUiState(
        category = data.category,
        total = data.totalExpenses,
        expenses = data.expenses,
        loadingState = LoadingState.Success(Unit)
    )

    companion object {
        val Idle = ExpenseListUiState(loadingState = LoadingState.Idle)
        val Empty = ExpenseListUiState(loadingState = LoadingState.Empty())
        val Error = ExpenseListUiState(loadingState = LoadingState.Empty())
        val Loading = ExpenseListUiState(loadingState = LoadingState.Loading)

        fun fromLoadingState(state: LoadingState<UiCategoryExpense>) = when (state) {
            is LoadingState.Empty -> {
                ExpenseListUiState(loadingState = LoadingState.Empty())
            }
            is LoadingState.Error ->  {
                ExpenseListUiState(loadingState = LoadingState.Error(state.message))
            }
            LoadingState.Idle -> {
                ExpenseListUiState(loadingState = LoadingState.Idle)
            }
            LoadingState.Loading -> {
                ExpenseListUiState(loadingState = LoadingState.Loading)
            }
            is LoadingState.Success -> {
                val data = state.data
                ExpenseListUiState(
                    category = data.category,
                    total = data.totalExpenses,
                    expenses = data.expenses,
                    loadingState = LoadingState.Success(Unit)
                )
            }
        }
    }
}