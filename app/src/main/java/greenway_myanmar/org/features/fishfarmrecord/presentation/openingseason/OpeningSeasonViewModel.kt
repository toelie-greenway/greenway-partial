package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCategoryExpensesUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCategoryExpensesUseCase.GetExpenseSummaryRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
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
    getCategoryExpensesStreamUseCase: GetCategoryExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpeningSeasonUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: OpeningSeasonUiState
        get() = uiState.value

    private val farmUiState = MutableStateFlow<FarmUiState>(LoadingState.Idle)
    val categoryListUiState: StateFlow<CategoryListUiState> = farmUiState
        .flatMapLatest { farmUiState ->
            categoryListUiStateStream(
                farmUiState,
                getCategoryExpensesStreamUseCase
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)


    init {

    }

    fun handleEvent(event: OpeningSeasonEvent) {
        when (event) {
            is OpeningSeasonEvent.OnSeasonIdChanged -> {
                updateSeasonId(event.seasonId)
            }
            is OpeningSeasonEvent.OnFarmChanged -> {
                updateFarm(event.farmUiState)
            }
        }
    }

    private fun updateFarm(farmUiState: LoadingState<UiFarm>) {
        this.farmUiState.value = farmUiState
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
    farmUiState: FarmUiState,
    getCategoryExpensesUseCase: GetCategoryExpensesUseCase
): Flow<CategoryListUiState> {
    return flow {
        emit(LoadingState.Loading)
        when (farmUiState) {
            is LoadingState.Success -> {
                val openingSeason = farmUiState.data.ongoingSeason
                if (openingSeason == null) {
                    emit(LoadingState.Empty(Text.ResourceText(R.string.ffr_farm_detail_label_no_ongoing_season)))
                } else {
                    emitAll(
                        loadSeasonExpenseCategories(openingSeason.id, getCategoryExpensesUseCase)
                    )
                }
            }
            LoadingState.Loading -> {
                emit(LoadingState.Loading)
            }
            else -> {
                emit(LoadingState.Idle)
            }
        }
    }
}

private suspend fun loadSeasonExpenseCategories(
    seasonId: String,
    getCategoryExpensesUseCase: GetCategoryExpensesUseCase
) = getCategoryExpensesUseCase(
    GetExpenseSummaryRequest(seasonId)
)
    .catch {
        LoadingState.Empty(it.errorText())
    }.map { result ->
        when (result) {
            is Result.Error -> {
                LoadingState.Error(result.exception.errorText())
            }
            Result.Loading -> {
                LoadingState.Loading
            }
            is Result.Success -> {
                val items = mutableListOf<OpeningSeasonCategoryListItemUiState>()
                val categories = result.data.map {
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
            }

        }
    }