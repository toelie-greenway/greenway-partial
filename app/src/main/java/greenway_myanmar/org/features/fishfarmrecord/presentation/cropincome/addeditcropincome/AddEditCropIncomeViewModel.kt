package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.addeditcropincome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCrop
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import greenway_myanmar.org.util.toKotlinInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditCropIncomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveCropIncomeUseCase: SaveCropIncomeUseCase
) : ViewModel() {

    private val args = AddEditCropIncomeFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val seasonId = args.seasonId

    private val _uiState = MutableStateFlow(AddEditCropIncomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _cropIncomeSavingUiState =
        MutableStateFlow<LoadingState<Unit>>(LoadingState.Idle)
    val cropIncomeSavingUiState = _cropIncomeSavingUiState.asStateFlow()

    private val currentUiState: AddEditCropIncomeUiState
        get() = uiState.value

    fun handleEvent(event: AddEditCropIncomeEvent) {
        when (event) {
            is AddEditCropIncomeEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditCropIncomeEvent.OnCropChanged -> {
                updateCrop(event.crop)
            }
            is AddEditCropIncomeEvent.OnPriceChanged -> {
                updatePrice(event.price)
            }
            AddEditCropIncomeEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditCropIncomeEvent.OnCropIncomeSavingErrorShown -> {
                clearCropIncomeSavingError()
            }
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun updateCrop(crop: UiCrop) {
        _uiState.value = currentUiState.copy(crop = crop)
    }

    private fun updatePrice(price: String) {
        _uiState.update {
            it.copy(price = price)
        }
    }

    private fun clearCropIncomeSavingError() {
        _cropIncomeSavingUiState.value = LoadingState.Idle
    }

    private fun onSubmit() {
        // validate inputs
        val cropValidationResult = validateCrop(currentUiState.crop)
        val priceValidationResult = validatePrice(currentUiState.price)

        // set/reset error
        _uiState.value = currentUiState.copy(
            cropError = cropValidationResult.getErrorOrNull(),
            priceError = priceValidationResult.getErrorOrNull()
        )

        // return if there is any error
        if (hasError(
                cropValidationResult,
                priceValidationResult
            )
        ) {
            return
        }

        // collect result
        val crop = cropValidationResult.getDataOrThrow()
        val price = priceValidationResult.getDataOrThrow()

        Timber.d("crop: $crop")
        Timber.d("price: $price")

        val request = SaveCropIncomeRequest(
            seasonId = seasonId,
            date = currentUiState.date.toKotlinInstant(),
            crop = crop.asDomainModel(),
            price = price
        )
        saveCropIncome(request)
    }

    private fun saveCropIncome(request: SaveCropIncomeRequest) {
        viewModelScope.launch {
            runCancellableCatching {
                Timber.d("Save crop income ... loading")
                _cropIncomeSavingUiState.value = LoadingState.Loading
                saveCropIncomeUseCase(request)
            }.onSuccess {
                Timber.d("Save crop income ... success")
                _cropIncomeSavingUiState.value = LoadingState.Success(Unit)
            }.onFailure {
                Timber.e(it, "Save crop income ... error")
                _cropIncomeSavingUiState.value = LoadingState.Error(it)
            }
        }
    }

    private fun validateCrop(crop: UiCrop?): ValidationResult<UiCrop> {
        return if (crop == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(crop)
        }
    }

    private fun validatePrice(priceAsString: String?): ValidationResult<BigDecimal> {
        val price = priceAsString?.toBigDecimalOrNull()
        return if (price == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(price)
        }
    }

}