package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductFilter
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductCategoriesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductsStreamUseCase.GetFarmInputProductsRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products.FarmInputProductPickerEvent.OnCategorySelectionChanged
import greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products.FarmInputProductPickerEvent.OnQueryChanged
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProductCategory
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FarmInputProductPickerViewModel
@Inject
constructor(
    getFarmInputProductsStreamUseCase: GetFarmInputProductsStreamUseCase,
    getFarmInputProductCategoriesStreamUseCase: GetFarmInputProductCategoriesStreamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmInputProductUiState())
    val uiState = _uiState.asStateFlow()

    private val filter: StateFlow<UiFarmInputProductFilter> = combine(
        uiState.map { it.query }.distinctUntilChanged(),
        uiState.map { it.category }.distinctUntilChanged(),
        ::Pair
    ).map { (query, category) ->
        UiFarmInputProductFilter(
            query = query,
            category = category
        )
    }.stateIn(viewModelScope, WhileViewSubscribed, UiFarmInputProductFilter())

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    val productsUiState: StateFlow<ProductsUiState> = filter.flatMapLatest {
        it.search { query, categoryId ->
            productsUiStateStream(
                query,
                categoryId,
                getFarmInputProductsStreamUseCase
            )
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    val categoriesUiState: StateFlow<CategoriesUiState> = loadDataSignal.flatMapLatest {
        categoriesUiStateStream(getFarmInputProductCategoriesStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    fun handleEvent(event: FarmInputProductPickerEvent) {
        when (event) {
            is OnCategorySelectionChanged -> {
                updateCategory(event.categoryIndex)
            }
            is OnQueryChanged -> {
                updateQuery(event.query)
            }
        }
    }

    private fun updateCategory(index: Int) {
        val categories = (categoriesUiState.value as? LoadingState.Success)?.data ?: return
        _uiState.update {
            it.copy(
                category = categories.getOrNull(index)
            )
        }
    }

    private fun updateQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }
}


private fun categoriesUiStateStream(
    getFarmInputProductCategoriesStreamUseCase: GetFarmInputProductCategoriesStreamUseCase,
): Flow<LoadingState<List<UiFarmInputProductCategory>>> {
    return getFarmInputProductCategoriesStreamUseCase()
        .catch {
            it.printStackTrace()
            LoadingState.Error(it.errorText())
        }
        .map { result ->
            Timber.d("Result: $result")
            when (result) {
                is Result.Success -> {
                    LoadingState.Success(
                        result.data.map {
                            UiFarmInputProductCategory.fromDomainModel(it)
                        }
                    )
                }
                is Result.Error -> {
                    result.exception?.printStackTrace()
                    LoadingState.Error(result.exception.errorText())
                }
                Result.Loading -> {
                    LoadingState.Loading
                }
            }
        }
}

private fun productsUiStateStream(
    query: String?,
    categoryId: String?,
    getFarmInputProductsStreamUseCase: GetFarmInputProductsStreamUseCase,
): Flow<ProductsUiState> {
    return getFarmInputProductsStreamUseCase(
        GetFarmInputProductsRequest(
            FarmInputProductFilter(
                categoryId = categoryId,
                query = query
            )
        )
    )
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    LoadingState.Success(
                        result.data.map {
                            UiFarmInputProduct.fromDomainModel(it)
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