package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetExpenseCategoriesWithTotalExpensesStreamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpeningSeasonViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow<OpeningSeasonUiState>(OpeningSeasonUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
        viewModelScope.launch {
            //TODO:
//            getExpenseCategoriesWithTotalExpensesStreamUseCase(Unit).collect { result ->
//                when (result) {
//                    is Result.Error -> {
//
//                    }
//                    Result.Loading -> {
//
//                    }
//                    is Result.Success -> {
//                        _uiState.update {
//                            it.copy(
//                                categories = result.data.map { categoryWithTotalExpenses ->
//                                    OpeningSeasonCategoryListItemUiState(
//                                        categoryId = categoryWithTotalExpenses.category.id,
//                                        categoryName = categoryWithTotalExpenses.category.name,
//                                        lastRecordDate = categoryWithTotalExpenses.lastRecordDate,
//                                        totalCategoryExpense = categoryWithTotalExpenses.totalExpenses
//                                    )
//                                },
//                                isCloseableSeason = true,
//                                isProducible = true,
//                                isFcrRecordable = true,
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}