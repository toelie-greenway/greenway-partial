package greenway_myanmar.org.features.fishfarmrecord.presentation.expensecategorypicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetExpenseCategoriesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import timber.log.Timber
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

    init {
        viewModelScope.launch {
            loadDataSignal.collect {
                Timber.d("loadDataSignal")
            }
        }
        viewModelScope.launch {
            refreshSignal.collect {
                Timber.d("refreshSignal")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val expenseCategoryListUiState: StateFlow<ExpenseCategoryListUiState> =
        loadDataSignal.transformLatest {
            emitAll(
                expenseCategoryListStream(
                    _selectedCategoryIdStream,
                    getExpenseCategoriesStreamUseCase
                )
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    fun handleEvent(event: ExpenseCategoryPickerEvent) {
        when (event) {
            is ExpenseCategoryPickerEvent.ToggleCategorySelection -> {
                toggleCategorySelection(event.categoryId)
            }
            ExpenseCategoryPickerEvent.RetryLoadingCategories -> {
                retryLoadingCategories()
            }
        }
    }

    private fun retryLoadingCategories() {
        viewModelScope.launch {
            refreshSignal.tryEmit(Unit)
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
                LoadingState.Success(
                    categories.map { category ->
                        ExpenseCategoryPickerListItemUiState(
                            category = UiExpenseCategory.fromDomain(category),
                            checked = selectedCategoryId == category.id
                        )
                    }
                )
            }
            is Result.Error -> {
                LoadingState.Error(result.exception.errorText())
            }
            Result.Loading -> {
                LoadingState.Loading
            }
        }
    }
}

//sealed interface ExpenseCategoryListUiState {
//    data class Success(val data: List<ExpenseCategoryPickerListItemUiState>) :
//        ExpenseCategoryListUiState
//
//    data class Error(val message: Text) : ExpenseCategoryListUiState
//    object Loading : ExpenseCategoryListUiState
//}
//
//fun ExpenseCategoryListUiState.asLoadingState() = when (this) {
//    is ExpenseCategoryListUiState.Error -> LoadingState.Error(message)
//    ExpenseCategoryListUiState.Loading -> LoadingState.Loading
//    is ExpenseCategoryListUiState.Success -> LoadingState.Success
//}

typealias ExpenseCategoryListUiState = LoadingState<List<ExpenseCategoryPickerListItemUiState>>