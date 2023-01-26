package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.labourcost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrDefault
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.isError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CalculateLabourCostUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.extensions.toIntOrZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class LabourCostInputViewModel @Inject constructor(
    private val calculateLabourCostUseCase: CalculateLabourCostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LabourCostInputUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: LabourCostInputUiState
        get() = uiState.value

    private val labourResourceCostStream: Flow<String?> = uiState.map { it.labourCost }
    private val animalResourceCostStream: Flow<String?> = uiState.map { it.animalResourceCost }

    init {
        observeCostChanges()
    }

    private fun observeCostChanges() {
        viewModelScope.launch {
            combine(
                labourResourceCostStream,
                animalResourceCostStream,
                ::Pair
            ).collect { (labourResourceCost, animalResourceCostStream) ->
                val result = calculateLabourCostUseCase(
                    CalculateLabourCostUseCase.CalculateLabourCostRequest(
                        labourResourceCost = labourResourceCost.toBigDecimalOrZero(),
                        animalResourceCost = animalResourceCostStream.toBigDecimalOrZero()
                    )
                )
                updateTotalCost(result.total)
            }
        }
    }

    fun handleEvent(event: LabourCostInputEvent) {
        when (event) {
            is LabourCostInputEvent.OnLabourQuantityChanged -> {
                updateLabourQuantity(event.quantity)
            }
            is LabourCostInputEvent.OnLabourCostChanged -> {
                updateLabourCost(event.cost)
            }
            is LabourCostInputEvent.OnAnimalResourceQuantityChanged -> {
                updateAnimalResourceQuantity(event.quantity)
            }
            is LabourCostInputEvent.OnAnimalResourceCostChanged -> {
                updateAnimalResourceCost(event.cost)
            }
            is LabourCostInputEvent.OnFamilyMemberQuantityChanged -> {
                updateFamilyMemberQuantity(event.quantity)
            }
            is LabourCostInputEvent.OnFamilyMemberCostChanged -> {
                updateFamilyMemberCost(event.cost)
            }
            LabourCostInputEvent.OnSubmit -> {
                onSubmit()
            }
        }
    }

    private fun updateLabourQuantity(quantity: String) {
        _uiState.value = currentUiState.copy(labourQuantity = quantity)
    }

    private fun updateLabourCost(cost: String) {
        _uiState.value = currentUiState.copy(labourCost = cost)
    }

    private fun updateAnimalResourceQuantity(quantity: String) {
        _uiState.value = currentUiState.copy(animalResourceQuantity = quantity)
    }

    private fun updateAnimalResourceCost(cost: String) {
        _uiState.value = currentUiState.copy(animalResourceCost = cost)
    }

    private fun updateFamilyMemberQuantity(quantity: String) {
        _uiState.value = currentUiState.copy(familyMemberQuantity = quantity)
    }

    private fun updateFamilyMemberCost(cost: String) {
        _uiState.value = currentUiState.copy(familyMemberCost = cost)
    }

    private fun updateTotalCost(total: BigDecimal) {
        _uiState.value = currentUiState.copy(totalCost = total)
    }

    /**
     * Validation logics for labour and animal resource cost
     * are a bit complex.
     * If the user entered quantity, he/she must enter cost and vice versa.
     * But, if he/she leaves empty for both quantity and cost, it's valid
     */
    private fun onSubmit() {
        var labourQuantity = 0
        var labourCost = BigDecimal.ZERO
        var animalResourceQuantity = 0
        var animalResourceCost = BigDecimal.ZERO
        val totalCost = currentUiState.totalCost
        val familyMemberQuantity = currentUiState.familyMemberQuantity.toIntOrZero()
        val familyMemberCost = currentUiState.familyMemberCost.toBigDecimalOrZero()

        // labour resource cost validation
        val labourResourceValidationResult =
            validateLabourResource(
                currentUiState.labourQuantity,
                currentUiState.labourCost
            )
        if (labourResourceValidationResult.isError()) {
            val labourQuantityValidationResult =
                validateLabourResourceQuantity(currentUiState.labourQuantity)
            val labourCostValidationResult = validateLabourResourceCost(currentUiState.labourCost)
            labourQuantity = labourQuantityValidationResult.getDataOrDefault(0)
            labourCost = labourCostValidationResult.getDataOrDefault(BigDecimal.ZERO)
            _uiState.value = currentUiState.copy(
                labourQuantityError = labourQuantityValidationResult.getErrorOrNull(),
                labourCostError = labourCostValidationResult.getErrorOrNull()
            )
        } else {
            _uiState.value = currentUiState.copy(
                labourCostError = null,
                labourQuantityError = null
            )
        }

        // animal resource cost validation
        val animalResourceValidationResult =
            validateAnimalResource(
                currentUiState.animalResourceQuantity,
                currentUiState.animalResourceCost
            )
        if (animalResourceValidationResult.isError()) {
            val animalResourceQuantityValidationResult =
                validateLabourResourceQuantity(currentUiState.animalResourceQuantity)
            val animalResourceCostValidationResult =
                validateLabourResourceCost(currentUiState.animalResourceCost)
            animalResourceQuantity = animalResourceQuantityValidationResult.getDataOrDefault(0)
            animalResourceCost =
                animalResourceCostValidationResult.getDataOrDefault(BigDecimal.ZERO)
            _uiState.value = currentUiState.copy(
                animalResourceQuantityError = animalResourceQuantityValidationResult.getErrorOrNull(),
                animalResourceCostError = animalResourceCostValidationResult.getErrorOrNull(),
            )
        } else {
            _uiState.value = currentUiState.copy(
                animalResourceQuantityError = null,
                animalResourceCostError = null
            )
        }

        if (hasError(
                labourResourceValidationResult,
                animalResourceValidationResult
            )
        ) {
            return
        }

        _uiState.value = currentUiState.copy(
            inputResult = UiLabourCost(
                labourQuantity = labourQuantity,
                labourCost = labourCost,
                animalResourceQuantity = animalResourceQuantity,
                animalResourceCost = animalResourceCost,
                familyMemberQuantity = familyMemberQuantity,
                familyMemberCost = familyMemberCost,
                totalCost = totalCost
            )
        )
    }

    private fun hasError(vararg validationResults: ValidationResult<Any>): Boolean {
        return validationResults.toList().any { !it.isSuccessful }
    }

    private fun validateLabourResource(
        labourQuantityString: String?,
        labourCostString: String?
    ): ValidationResult<Unit> {
        return if ((labourQuantityString.isNullOrEmpty()).xor(labourCostString.isNullOrEmpty())) {
            ValidationResult.Error()
        } else {
            ValidationResult.Success(Unit)
        }
    }

    private fun validateAnimalResource(
        animalResourceQuantityString: String?,
        animalResourceCostString: String?
    ): ValidationResult<Unit> {
        return if ((animalResourceQuantityString.isNullOrEmpty()).xor(animalResourceCostString.isNullOrEmpty())) {
            ValidationResult.Error()
        } else {
            ValidationResult.Success(Unit)
        }
    }

    private fun validateLabourResourceQuantity(
        labourQuantityString: String?
    ): ValidationResult<Int> {
        val labourQuantity = labourQuantityString.toIntOrZero()
        return if (!isInputQuantityValid(labourQuantity)) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(labourQuantity)
        }
    }

    private fun validateLabourResourceCost(
        labourCostString: String?
    ): ValidationResult<BigDecimal> {
        val labourCost = labourCostString.toBigDecimalOrZero()
        return if (!isInputCostValid(labourCost)) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(labourCost)
        }
    }

    private fun isInputQuantityValid(quantity: Int) = quantity != 0
    private fun isInputCostValid(cost: BigDecimal) = cost != BigDecimal.ZERO

}
