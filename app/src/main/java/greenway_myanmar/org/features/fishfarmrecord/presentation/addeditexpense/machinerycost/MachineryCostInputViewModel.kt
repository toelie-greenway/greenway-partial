package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.machinerycost

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MachineryCostInputViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(MachineryCostInputUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: MachineryCostInputUiState
        get() = uiState.value

    fun handleEvent(event: MachineryCostInputEvent) {
        when (event) {
            is MachineryCostInputEvent.OnMachineryCostChanged -> {
                updateMachineryCost(event.total)
            }
            MachineryCostInputEvent.OnSubmit -> {
                onSubmit()
            }
        }
    }

    private fun updateMachineryCost(total: String) {
        _uiState.value = currentUiState.copy(machineCost = total)
    }

    private fun onSubmit() {
        val machineryCostValidationResult = validateMachineryCost(currentUiState.machineCost)

        _uiState.value = currentUiState.copy(
            machineCostError = machineryCostValidationResult.getErrorOrNull()
        )

        if (hasError(machineryCostValidationResult)) {
            return
        }

        _uiState.value = currentUiState.copy(
            inputResult = UiMachineryCost(
                totalCost = machineryCostValidationResult.getDataOrThrow()
            )
        )
    }

    private fun hasError(vararg validationResults: ValidationResult<Any>): Boolean {
        return validationResults.toList().any { !it.isSuccessful }
    }

    private fun validateMachineryCost(costAsString: String?): ValidationResult<BigDecimal> {
        val cost = costAsString.toBigDecimalOrZero()
        return if (cost == BigDecimal.ZERO) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(cost)
        }
    }
}
