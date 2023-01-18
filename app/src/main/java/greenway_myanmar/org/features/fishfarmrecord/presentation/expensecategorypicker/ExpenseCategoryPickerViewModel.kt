package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetExpenseCategoriesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.ui.widget.LoadingState
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpenseCategoryPickerViewModel
@Inject
constructor(
    getExpenseCategoriesStreamUseCase: GetExpenseCategoriesStreamUseCase,
) : ViewModel() {

    private val _selectedCategoryIdStream = MutableStateFlow<String>("")
    private val currentCategoryId: String
        get() = _selectedCategoryIdStream.value

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    val expenseCategoryListUiState: StateFlow<ExpenseCategoryListUiState> =
        loadDataSignal.flatMapLatest {
            expenseCategoryListStream(
                _selectedCategoryIdStream,
                getExpenseCategoriesStreamUseCase
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, ExpenseCategoryListUiState.Loading)

    fun handleEvent(event: ExpenseCategoryPickerEvent) {
        when (event) {
            is ExpenseCategoryPickerEvent.ToggleCategorySelection -> {
                toggleCategorySelection(event.categoryId)
            }
        }
    }

    private fun toggleCategorySelection(categoryId: String) {
        val newCategoryId = if (currentCategoryId == categoryId) {
            ""
        } else {
            categoryId
        }
        _selectedCategoryIdStream.value = newCategoryId
    }
}

private fun expenseCategoryListStream(
    selectedFishIdsStream: Flow<String>,
    getExpenseCategoriesStreamUseCase: GetExpenseCategoriesStreamUseCase
): Flow<ExpenseCategoryListUiState> {
    return combine(
        getExpenseCategoriesStreamUseCase(),
        selectedFishIdsStream,
        ::Pair
    ).asResult().map { result ->
        when (result) {
            is Result.Success -> {
                val (categories, selectedCategoryId) = result.data
                ExpenseCategoryListUiState.Success(
                    categories.map { category ->
                        ExpenseCategoryPickerListItemUiState(
                            category = UiExpenseCategory.fromDomain(category),
                            checked = selectedCategoryId == category.id
                        )
                    }
                )
            }
            is Result.Error -> {
                ExpenseCategoryListUiState.Error(result.exception.errorText())
            }
            Result.Loading -> {
                ExpenseCategoryListUiState.Loading
            }
        }
    }
}

sealed interface ExpenseCategoryListUiState {
    data class Success(val data: List<ExpenseCategoryPickerListItemUiState>) :
        ExpenseCategoryListUiState

    data class Error(val message: Text) : ExpenseCategoryListUiState
    object Loading : ExpenseCategoryListUiState
}

fun ExpenseCategoryListUiState.asLoadingState() = when (this) {
    is ExpenseCategoryListUiState.Error -> LoadingState.Error(message)
    ExpenseCategoryListUiState.Loading -> LoadingState.Loading
    is ExpenseCategoryListUiState.Success -> LoadingState.Success
}