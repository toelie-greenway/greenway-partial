package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCategoryExpensesUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetCategoryExpensesUseCase.GetExpenseSummaryRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetProductionsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetProductionsStreamUseCase.GetProductionsRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarm
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeason
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    getCategoryExpensesStreamUseCase: GetCategoryExpensesUseCase,
    getProductionsStreamUseCase: GetProductionsStreamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OpeningSeasonUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: OpeningSeasonUiState
        get() = uiState.value

    private val farmUiState = MutableStateFlow<FarmUiState>(LoadingState.Idle)
    val categoryListUiState: StateFlow<CategoryListUiState> = farmUiState
        .flatMapLatest { farmUiState ->
            categoryListStream(
                farmUiState,
                getCategoryExpensesStreamUseCase,
                getProductionsStreamUseCase
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

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

private suspend fun categoryListStream(
    farmUiState: FarmUiState,
    getCategoryExpensesUseCase: GetCategoryExpensesUseCase,
    getProductionsStreamUseCase: GetProductionsStreamUseCase
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
                        loadCategoryListStream(
                            openingSeason,
                            getCategoryExpensesUseCase,
                            getProductionsStreamUseCase
                        )
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

private suspend fun loadCategoryListStream(
    openingSeason: UiSeason,
    getCategoryExpensesUseCase: GetCategoryExpensesUseCase,
    getProductionsStreamUseCase: GetProductionsStreamUseCase
) =
    combine(
        getCategoryExpensesUseCase(
            GetExpenseSummaryRequest(openingSeason.id)
        ),
        getProductionsStreamUseCase(
            GetProductionsRequest(openingSeason.id)
        ),
        ::Pair
    )
        .map { (expensesResult, productionsResult) ->
            if (expensesResult is Result.Success) {
                val items = mutableListOf<OpeningSeasonCategoryListItemUiState>()
                val categories = expensesResult.data.map {
                    OpeningSeasonCategoryListItemUiState.CategoryItem(
                        category = UiExpenseCategory.fromDomain(it.category),
                        lastRecordDate = it.lastRecordDate?.toJavaInstant(),
                        totalCategoryExpense = it.totalExpenses
                    )
                }
                items.addAll(categories)

                if (openingSeason.isHarvested) {
                    val productions = (productionsResult as? Result.Success)?.data
                    items.add(OpeningSeasonCategoryListItemUiState.ProductionItem(
                        lastRecordDate = productions?.maxByOrNull {
                            it.date
                        }?.date?.toJavaInstant(),
                        totalIncome = productions.orEmpty().sumOf {
                            it.totalPrice
                        }
                    ))
                }

                items.add(OpeningSeasonCategoryListItemUiState.CloseItem)

                LoadingState.Success(items)
            } else if (expensesResult is Result.Error || productionsResult is Result.Error) {
                val expenseError = (expensesResult as? Result.Error)?.exception
                val productionError = (productionsResult as? Result.Error)?.exception
                LoadingState.Error(exception = expenseError ?: productionError)
            } else if ((expensesResult is Result.Loading || productionsResult is Result.Loading)) {
                LoadingState.Loading
            } else {
                LoadingState.Idle
            }
        }
