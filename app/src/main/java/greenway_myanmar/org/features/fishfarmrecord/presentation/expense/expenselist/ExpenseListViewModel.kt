package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCategoryExpenseStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCategoryExpense
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    getCategoryExpenseUseCase: GetCategoryExpenseStreamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = ExpenseListFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val query = MutableStateFlow(Query.Empty)
    private val categoryExpenses: StateFlow<LoadingState<UiCategoryExpense>> =
        query.flatMapLatest { q ->
            q.ifExists { seasonId, categoryId ->
                categoryExpenseStream(seasonId, categoryId, getCategoryExpenseUseCase)
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    val uiState: StateFlow<ExpenseListUiState> = categoryExpenses.map {
        ExpenseListUiState.fromLoadingState(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, ExpenseListUiState.Empty)

    init {
        query.value = Query(
            seasonId = args.seasonId,
            categoryId = args.categoryId
        )
    }

    data class Query(
        val seasonId: String? = null,
        val categoryId: String? = null
    ) {
        fun <T> ifExists(f: (String, String) -> Flow<T>): Flow<T> {
            return if (seasonId.isNullOrEmpty() || categoryId.isNullOrEmpty()) {
                emptyFlow()
            } else {
                f(seasonId, categoryId)
            }
        }

        companion object {
            val Empty = Query()
        }
    }
}

private fun categoryExpenseStream(
    seasonId: String,
    categoryId: String,
    getCategoryExpenseUseCase: GetCategoryExpenseStreamUseCase
): Flow<LoadingState<UiCategoryExpense>> {
    return getCategoryExpenseUseCase(
        GetCategoryExpenseStreamUseCase.GetExpenseRequest(
            seasonId = seasonId,
            categoryId = categoryId
        )
    ).catch {
        LoadingState.Error(it.errorText())
    }.map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception.errorText())
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                if (result.data == null) {
                    LoadingState.Empty()
                } else {
                    LoadingState.Success(
                        data = UiCategoryExpense.fromDomainModel(result.data)
                    )
                }
            }
        }
    }
}