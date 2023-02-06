package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductCategoriesStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductsStreamUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmInputProductsStreamUseCase.GetFarmInputProductsRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FarmInputProductPickerViewModel
@Inject
constructor(
    getFarmInputProductsStreamUseCase: GetFarmInputProductsStreamUseCase,
    val getFarmInputProductCategoriesStreamUseCase: GetFarmInputProductCategoriesStreamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmInputProductUiState())
    val uiState = _uiState.asStateFlow()

    val productsUiState: StateFlow<ProductsUiState> = productsUiStateStream(
        getFarmInputProductsStreamUseCase,
        uiState.map { it.filter }
    ).stateIn(
        viewModelScope,
        WhileViewSubscribed,
        LoadingState.Idle
    )


    private val _categoriesUiState = MutableStateFlow<CategoriesUiState>(LoadingState.Idle)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    fun handleEvent(event: FarmInputProductPickerEvent) {

    }

//
//  val productCategories = _starterEvent.switchMap { productCategoryRepository.categories }
//  private val listing = _query.map { repository.loadProducts(it) }
//
//  val products = listing.switchMap { it.pagedList }
//  val networkState = listing.switchMap { it.networkState }
//  val refreshState = listing.switchMap { it.refreshState }
//
//  fun setCategoryId(categoryId: String?) {
//    val update = Query(categoryId, "")
//    if (Objects.equals(_query.value, update)) {
//      return
//    }
//    _query.value = update
//  }
//
//  fun setKeyword(keyword: String?) {
//    if (keyword.isNullOrBlank() && !_query.value?.categoryId.isNullOrBlank()) {
//      return
//    }
//
//    val update = Query("", keyword)
//    if (Objects.equals(_query.value, update)) {
//      return
//    }
//    _query.value = update
//  }
//
//  fun refresh() {
//    listing.value?.refreshCallback?.refresh()
//  }
//
//  fun retry() {
//    val listing = listing.value
//    listing?.retryCallback?.retry()
//  }
}

private fun productsUiStateStream(
    getFarmInputProductsStreamUseCase: GetFarmInputProductsStreamUseCase,
    filter: Flow<FarmInputProductFilterUiState?>
): Flow<LoadingState<List<UiFarmInputProduct>>> {
    return filter.flatMapLatest {
        getFarmInputProductsStreamUseCase(
            GetFarmInputProductsRequest(it?.toDomainModel())
        )
    }
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