package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.loanduration

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.loanduration.CustomLoanDurationUiState.CustomLoanDurationInputResult
import greenway_myanmar.org.util.extensions.toIntOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CustomLoadDurationInputViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(CustomLoanDurationUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: CustomLoanDurationUiState
        get() = uiState.value

    fun handleEvent(event: CustomLoadDurationEvent) {
        when (event) {
            is CustomLoadDurationEvent.OnMonthChanged -> {
                updateMonth(event.month)
            }
            CustomLoadDurationEvent.OnSubmit -> {
                onSubmit()
            }
        }
    }

    private fun updateMonth(month: String?) {
        _uiState.update {
            it.copy(month = month)
        }
    }

    private fun onSubmit() {
        val monthValidationResult = validateMonth(currentUiState.month)

        _uiState.value = currentUiState.copy(
            monthError = monthValidationResult.getErrorOrNull()
        )

        if (hasError(monthValidationResult)) {
            return
        }

        _uiState.value = currentUiState.copy(
            inputResult = CustomLoanDurationInputResult(
                month = monthValidationResult.getDataOrThrow()
            )
        )
    }

    private fun hasError(vararg validationResults: ValidationResult<Any>): Boolean {
        return validationResults.toList().any { !it.isSuccessful }
    }

    private fun validateMonth(monthAsString: String?): ValidationResult<Int> {
        val month = monthAsString.toIntOrZero()
        return if (month <= 0) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(month)
        }
    }
}