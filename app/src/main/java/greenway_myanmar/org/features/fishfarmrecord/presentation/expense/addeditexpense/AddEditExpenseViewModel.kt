package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import greenway_myanmar.org.util.toKotlinInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveExpenseUseCase: SaveExpenseUseCase
) : ViewModel() {

    private val args = AddEditExpenseFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val seasonId = args.seasonId

    private val _uiState = MutableStateFlow(AddEditExpenseUiState())
    val uiState = _uiState.asStateFlow()

    private val _expenseUploadingUiState =
        MutableStateFlow<ExpenseUploadingUiState>(LoadingState.Idle)
    val expenseUploadingUiState = _expenseUploadingUiState.asStateFlow()

    private val currentUiState: AddEditExpenseUiState
        get() = uiState.value

    fun handleEvent(event: AddEditExpenseEvent) {
        when (event) {
            is AddEditExpenseEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditExpenseEvent.OnCategoryChanged -> {
                updateCategory(event.category)
            }
            is AddEditExpenseEvent.OnLabourCostChanged -> {
                updateLabourCost(event.labourCost)
            }
            is AddEditExpenseEvent.OnMachineryCostChanged -> {
                updateMachineryCost(event.machineryCost)
            }
            is AddEditExpenseEvent.OnFarmInputAdded -> {
                updateFarmInput(event.farmInput)
            }
            is AddEditExpenseEvent.OnFarmInputRemoved -> {
                removeFarmInput(event.farmInput)
            }
            is AddEditExpenseEvent.OnNoteChanged -> {
                updateNote(event.note)
            }
            AddEditExpenseEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditExpenseEvent.CostErrorShown -> {
                clearCostError()
            }
            AddEditExpenseEvent.OnExpenseUploadingErrorShown -> {
                clearExpenseUploadingError()
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun updateCategory(category: UiExpenseCategory) {
        _uiState.value = currentUiState.copy(category = category)
    }

    private fun updateLabourCost(labourCost: UiLabourCost) {
        _uiState.value = currentUiState.copy(labourCost = labourCost)
    }

    private fun updateMachineryCost(machineryCost: UiMachineryCost) {
        _uiState.value = currentUiState.copy(machineryCost = machineryCost)
    }

    private fun updateFarmInput(farmInput: UiFarmInputCost) {
        _uiState.value = currentUiState.copy(
            farmInputCosts = currentUiState.farmInputCosts
                .removeIfExists(farmInput)
                .add(farmInput)
        )
    }

    private fun removeFarmInput(farmInput: UiFarmInputCost) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                farmInputCosts = currentUiState.farmInputCosts.filterNot { it.productId == farmInput.productId }
            )
        }
    }

    private fun List<UiFarmInputCost>.removeIfExists(
        item: UiFarmInputCost,
    ): List<UiFarmInputCost> {
        val resultList = this.toMutableList()
        val found = currentUiState.farmInputCosts.find { it.productId == item.productId }
        if (found != null) {
            resultList.remove(found)
        }
        return resultList
    }

    private fun List<UiFarmInputCost>.add(item: UiFarmInputCost): List<UiFarmInputCost> {
        val resultList = this.toMutableList()
        resultList.add(item)
        return resultList
    }

    private fun updateNote(note: String) {
        _uiState.update {
            it.copy(note = note)
        }
    }

    private fun clearCostError() {
        _uiState.update {
            it.copy(costError = null)
        }
    }

    private fun clearExpenseUploadingError() {
        _expenseUploadingUiState.value = LoadingState.Idle
    }

    private fun onSubmit() {
        // validate inputs
        val categoryValidationResult = validateCategory(currentUiState.category)
        val costsValidationResult = validateCosts(
            currentUiState.labourCost,
            currentUiState.machineryCost,
            currentUiState.farmInputCosts
        )

        // set/reset error
        _uiState.value = currentUiState.copy(
            categoryError = categoryValidationResult.getErrorOrNull(),
            costError = costsValidationResult.getErrorOrNull()
        )

        // return if there is any error
        if (hasError(
                categoryValidationResult,
                costsValidationResult
            )
        ) {
            return
        }

        // collect result
        val category = categoryValidationResult.getDataOrThrow()
        val labourCost = currentUiState.labourCost
        val machineryCost = currentUiState.machineryCost
        val farmInputCosts = currentUiState.farmInputCosts

        Timber.d("Category: $category")
        Timber.d("labourCost: $labourCost")
        Timber.d("machineryCost: $machineryCost")
        Timber.d("farmInputCosts: $farmInputCosts")

        val request = SaveExpenseUseCase.SaveExpenseRequest(
            seasonId = seasonId,
            expenseCategory = category.asDomainModel(),
            date = currentUiState.date.toKotlinInstant(),
            labourQuantity = labourCost?.labourQuantity,
            labourCost = labourCost?.labourCost,
            familyQuantity = labourCost?.familyMemberQuantity,
            familyCost = labourCost?.familyMemberCost,
            machineryCost = machineryCost?.totalCost,
            images = emptyList(), // TODO:
            remark = currentUiState.note,
            inputs = farmInputCosts.map(UiFarmInputCost::asDomainModel)
        )
        saveExpense(request)
    }

    private fun saveExpense(request: SaveExpenseUseCase.SaveExpenseRequest) {
        viewModelScope.launch {
            runCancellableCatching {
                Timber.d("Save expense ... loading")
                _expenseUploadingUiState.value = LoadingState.Loading
                saveExpenseUseCase(request)
            }.onSuccess { result ->
                Timber.d("Save expense ... success")
                _expenseUploadingUiState.value = LoadingState.Success(Unit)
                _uiState.update {
                    it.copy(
                        addEditExpenseResult = AddEditExpenseUiState.AddEditExpenseResult(
                            expenseId = result.id
                        )
                    )
                }
            }.onFailure {
                Timber.e(it, "Save expense ... error")
                _expenseUploadingUiState.value = LoadingState.Error(it.errorText())
            }
        }
    }

    private fun validateCategory(category: UiExpenseCategory?): ValidationResult<UiExpenseCategory> {
        return if (category == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(category)
        }
    }

    private fun validateCosts(
        labourCost: UiLabourCost?,
        machineryCost: UiMachineryCost?,
        farmInputCosts: List<UiFarmInputCost>?
    ): ValidationResult<Unit> {
        return if (labourCost == null && machineryCost == null && farmInputCosts.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.ffr_add_edit_expense_error_cost_required))
        } else {
            ValidationResult.Success(Unit)
        }
    }
}