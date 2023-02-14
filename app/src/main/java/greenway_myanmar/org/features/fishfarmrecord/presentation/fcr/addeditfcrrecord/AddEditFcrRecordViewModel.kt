package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord

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
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views.FcrRatioInputErrorUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views.FcrRatioInputUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinInstant
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class AddEditFcrRecordViewModel @Inject constructor(
    private val saveFcrRecordUseCase: SaveFcrRecordUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = AddEditFcrRecordFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _uiState = MutableStateFlow(AddEditFcrRecordUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditFcrRecordUiState
        get() = uiState.value

    init {
        _uiState.value = currentUiState.copy(
            fishes = args.fishes.toList()
        )
        calculateRatios()
    }

    private fun calculateRatios() {
        viewModelScope.launch {
            combine(
                uiState.map { it.fishes }.distinctUntilChanged(),
                uiState.map { it.feedWeights }.distinctUntilChanged(),
                uiState.map { it.gainWeights }.distinctUntilChanged(),
            ) { fishes, feedWeights, gainWeights ->
                val map = mutableMapOf<Int, BigDecimal?>()
                List(fishes.size) { index ->
                    val feedWeight = feedWeights[index]?.toBigDecimalOrNull()
                    val gainWeight = gainWeights[index]?.toBigDecimalOrNull()
                    if (feedWeight != null && gainWeight != null) {
                        map[index] = try {
                            feedWeight.divide(gainWeight, 2, RoundingMode.HALF_UP)
                        } catch (e: ArithmeticException) {
                            BigDecimal.ZERO
                        }
                    } else {
                        map[index] = null
                    }
                }
                map
            }.collect { ratios ->
                _uiState.update {
                    it.copy(calculatedRatios = ratios)
                }
            }
        }
    }

    fun handleEvent(event: AddEditFcrRecordEvent) {
        when (event) {
            is AddEditFcrRecordEvent.OnDateChanged -> {
                updateDate(event.date)
            }
            is AddEditFcrRecordEvent.OnFeedWeightChanged -> {
                updateFeedWeight(event.index, event.weight)
            }
            is AddEditFcrRecordEvent.OnGainWeightChanged -> {
                updateGainWeight(event.index, event.weight)
            }
            AddEditFcrRecordEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditFcrRecordEvent.AllInputErrorShown -> {
                clearAllInputError()
            }
            AddEditFcrRecordEvent.OnSavingFcrRecordErrorShown -> {
                clearSavingFcrRecordError()
            }
        }
    }

    private fun updateFeedWeight(index: Int, weight: String?) {
        val currentWeight = currentUiState.feedWeights[index]
        if (currentWeight == weight) {
            return
        }

        val newFeedWeights = currentUiState.feedWeights.toMutableMap()
        newFeedWeights[index] = weight
        _uiState.update {
            it.copy(feedWeights = newFeedWeights)
        }
    }

    private fun updateGainWeight(index: Int, weight: String?) {
        val currentWeight = currentUiState.gainWeights[index]
        if (currentWeight == weight) {
            return
        }

        val newGainWeights = currentUiState.gainWeights.toMutableMap()
        newGainWeights[index] = weight
        _uiState.update {
            it.copy(gainWeights = newGainWeights)
        }
    }

    private fun updateDate(date: LocalDate) {
        _uiState.value = currentUiState.copy(date = date)
    }

    private fun clearAllInputError() {
        _uiState.update {
            it.copy(allInputError = null)
        }
    }

    private fun clearSavingFcrRecordError() {
        _uiState.update {
            it.copy(fcrRecordSavingError = null)
        }
    }

    private fun onSubmit() {
        // validate inputs
        val dateValidationResult = validateDate(currentUiState.date)
        val allWeightInputsValidationResult: ValidationResult<Unit> = validateAllWeightInputs()
        val individualWeightInputsValidationResult: List<ValidationResult<Pair<Int, FcrRatioInputUiState>>> =
            validateIndividualWeightInputs()
        val individualWeightInputErrors = individualWeightInputsValidationResult.getErrorsOrNull()
        val individualWeightInputResult = individualWeightInputsValidationResult.getData()

        // set/reset error
        _uiState.value = currentUiState.copy(
            dateError = dateValidationResult.getErrorOrNull(),
            allInputError = allWeightInputsValidationResult.getErrorOrNull(),
            individualInputErrors = individualWeightInputErrors
        )

        // return if there is any error
        if (hasError(
                dateValidationResult,
                allWeightInputsValidationResult,
            ) || !individualWeightInputErrors.isNullOrEmpty()
        ) {
            return
        }

        // collect result
        val date = dateValidationResult.getDataOrThrow()

        Timber.d("Date: $date")
        Timber.d("Ratios: $individualWeightInputResult")
        val request = SaveFcrRecordRequest(
            id = null, //TODO: update when support edit
            date = date.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toKotlinInstant(),
            ratios = individualWeightInputResult.map {
                it.asDomainModel()
            },
            seasonId = args.seasonId
        )
        saveFcrRecord(request)
    }

    private fun validateDate(date: LocalDate?): ValidationResult<LocalDate> {
        return if (date == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(date)
        }
    }

    private fun validateAllWeightInputs(): ValidationResult<Unit> {
        List(currentUiState.fishes.size) { index ->
            val feedWeight = currentUiState.feedWeights[index]
            val gainWeight = currentUiState.gainWeights[index]

            if (!feedWeight.isNullOrEmpty() || !gainWeight.isNullOrEmpty()) {
                return ValidationResult.Success(Unit)
            }
        }
        return ValidationResult.Error(Text.ResourceText(R.string.ffr_add_edit_fcr_error_weight_required))
    }

    private fun validateIndividualWeightInputs(): List<ValidationResult<Pair<Int, FcrRatioInputUiState>>> {
        return currentUiState.fishes.mapIndexed { index, fish ->
            val feedWeight = currentUiState.feedWeights[index]
            val gainWeight = currentUiState.gainWeights[index]
            validateWeightInput(index, fish, feedWeight, gainWeight)
        }
    }

    private fun validateWeightInput(
        index: Int,
        fish: UiFish,
        feedWeightString: String?,
        gainWeightString: String?
    ): ValidationResult<Pair<Int, FcrRatioInputUiState>> {
        val feedWeight = feedWeightString.toBigDecimalOrZero()
        val gainWeight = gainWeightString.toBigDecimalOrZero()
        return if (feedWeightString.isNullOrEmpty().xor(gainWeightString.isNullOrEmpty())) {
            ValidationResult.Error(
                message = Text.ResourceText(R.string.error_field_required),
                data = Pair(
                    index,
                    FcrRatioInputUiState(
                        fish = fish,
                        feedWeight = feedWeight,
                        gainWeight = gainWeight
                    )
                )
            )
        } else {
            ValidationResult.Success(
                data = Pair(
                    index,
                    FcrRatioInputUiState(
                        fish = fish,
                        feedWeight = feedWeight,
                        gainWeight = gainWeight
                    )
                )
            )
        }
    }

    private fun List<ValidationResult<Pair<Int, FcrRatioInputUiState>>>.getErrorsOrNull(): Map<Int, FcrRatioInputErrorUiState?>? {
        if (this.all { it.isSuccessful }) {
            return null
        }

        val map = mutableMapOf<Int, FcrRatioInputErrorUiState>()
        this.forEach { result ->
            if (result is ValidationResult.Error) {
                result.data?.let { (index, fcrRatioInputUiState) ->
                    map.put(
                        index, FcrRatioInputErrorUiState(
                            feedWeightError = fcrRatioInputUiState.getFeedWeightErrorOrNull(result.getErrorOrNull()),
                            gainWeightError = fcrRatioInputUiState.getGainWeightErrorOrNull(result.getErrorOrNull())
                        )
                    )
                }
            }
        }
        return map
    }

    private fun List<ValidationResult<Pair<Int, FcrRatioInputUiState>>>.getData(): List<FcrRatioInputUiState> {
        val list = mutableListOf<FcrRatioInputUiState>()
        this.forEach { result ->
            if (result is ValidationResult.Success) {
                result.data.let { (_, fcrRatioInputUiState) ->
                    list.add(fcrRatioInputUiState)
                }
            }
        }
        return list
    }

    private fun saveFcrRecord(request: SaveFcrRecordRequest) {
        viewModelScope.launch {
            runCancellableCatching {
                _uiState.update {
                    it.copy(
                        fcrRecordSavingState = LoadingState.Loading
                    )
                }
                saveFcrRecordUseCase(request)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        fcrRecordSavingState = LoadingState.Success(Unit)
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        fcrRecordSavingState = LoadingState.Error(exception)
                    )
                }
            }
        }
    }
}
