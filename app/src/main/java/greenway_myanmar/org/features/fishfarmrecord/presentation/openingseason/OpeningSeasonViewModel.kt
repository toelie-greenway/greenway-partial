package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetExpensesByCategoryUseCase
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.toJavaInstant
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class OpeningSeasonViewModel @Inject constructor(
    getExpensesByCategoryStreamUseCase: GetExpensesByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpeningSeasonUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: OpeningSeasonUiState
        get() = uiState.value

    val categoryListUiState: StateFlow<CategoryListUiState> = uiState.map { it.seasonId }
        .distinctUntilChanged()
        .flatMapLatest { seasonId ->
            categoryListUiStateStream(
                seasonId,
                getExpensesByCategoryStreamUseCase
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)


    init {

    }

    fun handleEvent(event: OpeningSeasonEvent) {
        when (event) {
            is OpeningSeasonEvent.OnSeasonIdChanged -> {
                updateSeasonId(event.seasonId)
            }
        }
    }

    private fun updateSeasonId(seasonId: String) {
        if (currentUiState.seasonId == seasonId) {
            return
        }
        _uiState.update {
            it.copy(seasonId = seasonId)
        }
    }
}

private suspend fun categoryListUiStateStream(
    seasonId: String,
    getExpensesByCategoryUseCase: GetExpensesByCategoryUseCase
): Flow<CategoryListUiState> {
    if (seasonId.isEmpty()) return emptyFlow()

    return flow {
        emit(LoadingState.Loading)

        emit(
            try {
                val result = getExpensesByCategoryUseCase(
                    GetExpensesByCategoryUseCase.GetExpensesByCategoryRequest(seasonId)
                )
                val items = mutableListOf<OpeningSeasonCategoryListItemUiState>()
                val categories = result.map {
                    OpeningSeasonCategoryListItemUiState.CategoryItem(
                        categoryId = it.category.id,
                        categoryName = it.category.name,
                        lastRecordDate = it.lastRecordDate?.toJavaInstant(),
                        totalCategoryExpense = it.totalExpenses
                    )
                }
                items.addAll(categories)

//                val harvested = result.firstOrNull { it.category.isHarvesting && it.lastRecordDate != null} != null
//                if(harvested) {
//                    items.add(OpeningSeasonCategoryListItemUiState.ProductionItem())
//                }
                items.add(OpeningSeasonCategoryListItemUiState.CloseItem)

                LoadingState.Success(items)
            } catch (e: Throwable) {
                LoadingState.Empty(e.errorText())
            }
        )
    }
}