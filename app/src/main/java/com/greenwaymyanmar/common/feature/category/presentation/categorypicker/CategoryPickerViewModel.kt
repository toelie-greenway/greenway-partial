package com.greenwaymyanmar.common.feature.category.presentation.categorypicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.feature.category.domain.usecase.GetCategoriesStreamUseCase
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategoryType
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryPickerViewModel
@Inject
constructor(
    getCategoriesStreamUseCase: GetCategoriesStreamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args =
        CategoryPickerBottomSheetFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _categoryTypeStream = MutableStateFlow(UiCategoryType.Unknown)

    private val _selectedCategoryIdStream = MutableStateFlow("")
    private val currentCategoryId: String
        get() = _selectedCategoryIdStream.value

    private val refreshSignal = MutableSharedFlow<Unit>()
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val categories: StateFlow<LoadingState<List<CategoryPickerListItemUiState>>> =
        _categoryTypeStream
            .transformLatest { categoryType ->
                emitAll(
                    categoriesStream(
                        categoryType,
                        _selectedCategoryIdStream,
                        getCategoriesStreamUseCase
                    )
                )
            }
            .stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)


    init {
        _categoryTypeStream.value = args.categoryType

    }

    fun handleEvent(event: CategoryPickerEvent) {
        when (event) {
            is CategoryPickerEvent.ToggleCategorySelection -> {
                toggleCategorySelection(event.categoryId)
            }
            CategoryPickerEvent.RetryLoadingCategories -> {
                retryLoadingCategories()
            }
        }
    }

    private fun retryLoadingCategories() {
        viewModelScope.launch { refreshSignal.tryEmit(Unit) }
    }

    private fun toggleCategorySelection(categoryId: String) {
        val newCategoryId =
            if (currentCategoryId == categoryId) {
                ""
            } else {
                categoryId
            }
        _selectedCategoryIdStream.value = newCategoryId
    }
}

private fun categoriesStream(
    categoryType: UiCategoryType,
    selectedCategoryIdStream: Flow<String>,
    getCategoriesStreamUseCase: GetCategoriesStreamUseCase
): Flow<LoadingState<List<CategoryPickerListItemUiState>>> {
    if (categoryType == UiCategoryType.Unknown) return emptyFlow()

    return combine(
        getCategoriesStreamUseCase(
            GetCategoriesStreamUseCase.GetCategoriesRequest(
                categoryType.toDomainModel()
            )
        ),
        selectedCategoryIdStream,
        ::Pair
    )
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    val (categories, selectedCategoryId) = result.data
                    LoadingState.Success(
                        categories.map { category ->
                            CategoryPickerListItemUiState(
                                category = UiCategory.fromDomainModel(category),
                                checked = selectedCategoryId == category.id
                            )
                        }
                    )
                }
                is Result.Error -> {
                    LoadingState.Error(result.exception)
                }
                Result.Loading -> {
                    LoadingState.Loading
                }
            }
        }
}