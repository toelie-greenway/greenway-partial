package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.model.UiUnitOfMeasurement
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorsOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CalculateAdvancedFarmInputCostUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CalculateAdvancedFarmInputCostUseCase.CalculateAdvancedFarmInputCostRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CalculateSimpleFarmInputCostUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CalculateSimpleFarmInputCostUseCase.CalculateSimpleFarmInputCostRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class FarmInputInputViewModel @Inject constructor(
    calculateSimpleFarmInputCostUseCase: CalculateSimpleFarmInputCostUseCase,
    calculateAdvancedFarmInputCostUseCase: CalculateAdvancedFarmInputCostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmInputInputUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: FarmInputInputUiState
        get() = uiState.value

    val selectedProduct
        get() = uiState.value.product

    private val _unitsUiState = MutableStateFlow<UnitsUiState>(
        LoadingState.Idle
    )
    val unitsUiState = _unitsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                uiState.map { it.isAdvanced }.distinctUntilChanged(),
                uiState.map { it.usedAmount }.distinctUntilChanged(),
                uiState.map { it.usedUnitPrice }.distinctUntilChanged()
            ) { isAdvanced, usedAmount, usedUnitPrice ->
                if (isAdvanced) {
                    calculateAdvancedFarmInputCostUseCase(
                        CalculateAdvancedFarmInputCostRequest(
                            pricePerPackage = BigDecimal.ZERO,
                            amountPerPackage = 0.0,
                            usedAmount = 0.0
                        )
                    ).total
                } else {
                    calculateSimpleFarmInputCostUseCase(
                        CalculateSimpleFarmInputCostRequest(
                            unitPrice = usedUnitPrice.toBigDecimalOrZero(),
                            amount = usedAmount?.toDoubleOrNull() ?: 0.0
                        )
                    ).total
                }
            }.collect { total ->
                updateTotalCost(total)
            }
        }
        _unitsUiState.value = LoadingState.Success(
            listOf(UiUnitOfMeasurement("Kg"), UiUnitOfMeasurement("lb")).toList()
        )
    }

    fun handleEvent(event: FarmInputInputEvent) {
        when (event) {
            is FarmInputInputEvent.OnFarmInputProductChanged -> {
                updateProduct(event.product)
            }
            is FarmInputInputEvent.OnUsedAmountChanged -> {
                updateUsedAmount(event.amount)
            }
            is FarmInputInputEvent.OnUsedUnitSelectionChanged -> {
                updateUsedUnit(event.position)
            }
            is FarmInputInputEvent.OnUsedUnitPriceChanged -> {
                updateUsedUnitPrice(event.unitPrice)
            }
            FarmInputInputEvent.OnSubmit -> {
                onSubmit()
            }
        }
    }

    private fun updateProduct(product: UiFarmInputProduct) {
        _uiState.value = currentUiState.copy(product = product)
        _unitsUiState.value = LoadingState.Success(product.units)
    }

    private fun updateUsedAmount(amount: String) {
        _uiState.value = currentUiState.copy(usedAmount = amount)
    }

    private fun updateUsedUnit(position: Int) {
        val units = (unitsUiState.value as? LoadingState.Success)?.data ?: return
        val selectedUnit = units[position]
        _uiState.value = currentUiState.copy(
            usedUnit = selectedUnit
        )
    }

    private fun updateUsedUnitPrice(unitPrice: String) {
        _uiState.value = currentUiState.copy(usedUnitPrice = unitPrice)
    }

    private fun updateTotalCost(total: BigDecimal) {
        _uiState.value = currentUiState.copy(totalCost = total)
    }

    private fun onSubmit() {
        // validate inputs
        val productValidationResult = validateProduct(currentUiState.product)
        val usedAmountValidationResult = validateUsedAmount(currentUiState.usedAmount)
        val usedUnitValidationResult = validateUsedUnit(currentUiState.usedUnit)
        val usedUnitPriceValidationResult = validateUsedUnitPrice(currentUiState.usedUnitPrice)
        val totalCostValidationResult = validateTotalCost(currentUiState.totalCost)

        // set/reset error
        _uiState.value = currentUiState.copy(
            productError = productValidationResult.getErrorsOrNull(),
            usedAmountError = usedAmountValidationResult.getErrorsOrNull(),
            usedUnitError = usedUnitValidationResult.getErrorsOrNull(),
            usedUnitPriceError = usedUnitPriceValidationResult.getErrorsOrNull(),
            totalCostError = totalCostValidationResult.getErrorsOrNull()
        )

        // return if there is any error
        if (hasError(
                productValidationResult,
                usedAmountValidationResult,
                usedUnitValidationResult,
                usedUnitPriceValidationResult,
                totalCostValidationResult
            )
        ) {
            return
        }

        // collect result
        val product = productValidationResult.getDataOrThrow()
        val usedAmount = usedAmountValidationResult.getDataOrThrow()
        val usedUnit = usedUnitValidationResult.getDataOrThrow()
        val usedUnitPrice = usedUnitPriceValidationResult.getDataOrThrow()
        val totalCost = totalCostValidationResult.getDataOrThrow()
        _uiState.value = currentUiState.copy(
            inputResult = FarmInputInputResult(
                product = product,
                usedAmount = usedAmount,
                usedUnit = usedUnit,
                usedUnitPrice = usedUnitPrice,
                totalCost = totalCost,
            )
        )
    }

    private fun validateProduct(product: UiFarmInputProduct?): ValidationResult<UiFarmInputProduct> {
        return if (product == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(product)
        }
    }

    private fun validateUsedAmount(usedAmountString: String?): ValidationResult<Double> {
        val usedAmount = usedAmountString?.toDoubleOrNull()
        return if (usedAmount == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(usedAmount)
        }
    }

    private fun validateUsedUnit(unit: UiUnitOfMeasurement?): ValidationResult<UiUnitOfMeasurement> {
        return if (unit == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(unit)
        }
    }

    private fun validateUsedUnitPrice(unitPriceString: String?): ValidationResult<BigDecimal> {
        val unitPrice = unitPriceString.toBigDecimalOrZero()
        return if (unitPrice == BigDecimal.ZERO) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(unitPrice)
        }
    }

    private fun validateTotalCost(totalCost: BigDecimal?): ValidationResult<BigDecimal> {
        return if (totalCost == null || totalCost <= BigDecimal.ZERO) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(totalCost)
        }
    }
}

typealias UnitsUiState = LoadingState<List<UiUnitOfMeasurement>>