package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.addeditfarm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.model.asDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorsOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmOwnership
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddEditFarmViewModel @Inject constructor(
    private val saveFarmUseCase: SaveFarmUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditFarmUiState
        get() = uiState.value

    fun handleEvent(event: AddEditFarmEvent) {
        when (event) {
            is AddEditFarmEvent.OnFarmNameChanged -> {
                updateFarmName(event.name)
            }
            is AddEditFarmEvent.OnFarmAreaChanged -> {
                updateFarmArea(event.area)
            }
            is AddEditFarmEvent.OnFarmAreaMeasurementChanged -> {
                updateFarmAreaMeasurement(event.measurement)
            }
            is AddEditFarmEvent.OnFarmImageChanged -> {
                updateFarmImage(event.imageUri)
            }
            is AddEditFarmEvent.OnFarmOwnershipChanged -> {
                updateFarmOwnership(event.ownership)
            }
            is AddEditFarmEvent.OnDetailChanged -> {
                updateDetail(event.showDetail)
            }
            is AddEditFarmEvent.OnFarmDepthChanged -> {
                updateFarmDepth(event.depth)
            }
            is AddEditFarmEvent.OnFarmUPaingNumberChanged -> {
                updateFarmUPaingNumber(event.uPaingNumber)
            }
            is AddEditFarmEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditFarmEvent.OnCreatedFarmHandled -> {
                onCreatedPondHandled()
            }
        }
    }

    private fun updateFarmName(pondName: String?) {
        _uiState.update {
            it.copy(farmName = pondName)
        }
    }

    private fun updateFarmAreaMeasurement(measurement: AreaMeasurement) {
        _uiState.update {
            it.copy(
                farmMeasurement = measurement
            )
        }

        if (measurement is AreaMeasurement.Area) {
            viewModelScope.launch {
                // FIXME: Have to use delay because the measured farm area
                //  is overridden by EditText's doAfterTextChanged on resume
                delay(300)
                _uiState.update {
                    it.copy(
                        farmArea = measurement.acre.toString()
                    )
                }
            }
        }
    }

    private fun updateFarmArea(area: String?) {
        _uiState.update {
            it.copy(farmArea = area)
        }
    }

    private fun updateFarmImage(imageUri: Uri) {
        if (currentUiState.farmImageUri == imageUri) {
            return
        }

        _uiState.value = currentUiState.copy(
            farmImageUri = imageUri
        )
    }

    private fun updateFarmOwnership(ownership: UiFarmOwnership?) {
        _uiState.update {
            it.copy(
                farmOwnership = ownership
            )
        }
    }

    private fun updateDetail(showDetail: Boolean) {
        _uiState.update {
            it.copy(inputFarmDetail = showDetail)
        }
    }

    private fun updateFarmDepth(depth: String?) {
        _uiState.update {
            it.copy(farmDepth = depth)
        }
    }

    private fun updateFarmUPaingNumber(uPaingNumber: String?) {
        _uiState.update {
            it.copy(plotId = uPaingNumber)
        }
    }

    private fun onCreatedPondHandled() {
        TODO()
    }

    private fun onSubmit() {
        // validate inputs
        val nameValidationResult = validateFarmName(currentUiState.farmName)
        val areaValidationResult = validateFarmArea(currentUiState.farmArea)
        val ownershipValidationResult = validateFarmOwnership(currentUiState.farmOwnership)

        // set/reset error
        _uiState.value = currentUiState.copy(
            farmNameError = nameValidationResult.getErrorsOrNull(),
            farmAreaError = areaValidationResult.getErrorsOrNull(),
            farmOwnershipError = ownershipValidationResult.getErrorsOrNull()
        )

        // return if there is any error
        if (hasError(
                nameValidationResult,
                areaValidationResult,
                ownershipValidationResult
            )
        ) {
            return
        }

        // collect result
        val name = nameValidationResult.getDataOrThrow()
        val area = areaValidationResult.getDataOrThrow()
        val measurement = currentUiState.farmMeasurement
        var measureMethod: AreaMeasureMethod? = null
        var location: LatLng? = null
        var coordinates: List<LatLng>? = null
        var measuredArea: Double? = null
        when (measurement) {
            is AreaMeasurement.Location -> {
                location = measurement.latLng
                measureMethod = measurement.measurementType
            }
            is AreaMeasurement.Area -> {
                measuredArea = measurement.acre
                coordinates = measurement.coordinates
                measureMethod = measurement.measurementType
            }
            else -> {
                /* no-op */
            }
        }
        val farmImageUri = currentUiState.farmImageUri
        val ownership = ownershipValidationResult.getDataOrThrow()
        val plotId = currentUiState.plotId
        val farmDepth = currentUiState.farmDepth?.toDoubleOrNull()
        Timber.d("Name: $name")
        Timber.d("Area: $area")
        Timber.d("Location: $location")
        Timber.d("Coordinates: $coordinates")
        Timber.d("Measured area: $measuredArea")
        Timber.d("Farm Image Uri: $farmImageUri")
        Timber.d("Farm UPaingNumber: $plotId")
        Timber.d("Farm Depth: $farmDepth")

        saveFarm(
            SaveFarmUseCase.SaveFarmRequest(
                id = "", //TODO: change when support edit
                name = name,
                measurement = FarmMeasurement(
                    location = location,
                    coordinates = coordinates,
                    area = area.asDomain(),
                    measuredArea = Area.acreOrNull(measuredArea),
                    measuredType = measureMethod
                ),
                ownership = ownership.asDomainModel(),
                imageUri = farmImageUri,
                plotId = plotId
            )
        )
    }

    private fun saveFarm(saveFarmRequest: SaveFarmUseCase.SaveFarmRequest) {
        viewModelScope.launch {
            try {
                val result = saveFarmUseCase(saveFarmRequest)
                _uiState.update {
                    it.copy(
                        newFarmResult = AddEditFarmUiState.AddEditFarmResult(
                            farmId = result.id
                        )
                    )
                }
            } catch (e: Exception) {
                // TODO: Handle exception
                e.printStackTrace()
            }
        }
    }

    private fun validateFarmName(name: String?): ValidationResult<String> {
        return if (name.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(name)
        }
    }

    private fun validateFarmArea(areaString: String?): ValidationResult<UiArea> {
        val area = areaString?.toDoubleOrNull()
        return if (area == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(UiArea.acre(area))
        }
    }

    private fun validateFarmOwnership(ownership: UiFarmOwnership?): ValidationResult<UiFarmOwnership> {
        return if (ownership == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(ownership)
        }
    }
}