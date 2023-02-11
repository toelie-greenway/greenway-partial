package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.model.asDomain
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Loan
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetContractFarmingCompanyByCode
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmMeasurementUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFarmMeasurementUseCase.GetFarmMeasurementRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLoanDuration
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asDomainModel
import greenway_myanmar.org.util.WhileViewSubscribed
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.toKotlinInstant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditSeasonViewModel @Inject constructor(
    private val getContractFarmingCompanyByCode: GetContractFarmingCompanyByCode,
    private val getFarmMeasurementUseCase: GetFarmMeasurementUseCase,
    private val saveSeasonUseCase: SaveSeasonUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val farmIdStream = MutableStateFlow("")
    private val farmId = AddEditSeasonFragmentArgs.fromSavedStateHandle(savedStateHandle).farmId

    private val _uiState = MutableStateFlow(AddEditSeasonUiState())
    val uiState = _uiState.asStateFlow()

    val farmMeasurementUiState: StateFlow<FarmMeasurementUiState> =
        farmIdStream.flatMapLatest { farmId ->
            farmMeasurementStream(
                farmId = farmId,
                getFarmMeasurementUseCase = getFarmMeasurementUseCase,
                onFarmMeasurementLoaded = {
                    onFarmMeasurementLoaded(it)
                }
            )
        }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    private val _seasonUploadingUiState = MutableStateFlow<SeasonUploadingUiState>(LoadingState.Idle)
    val seasonUploadingUiState = _seasonUploadingUiState.asStateFlow()

    private val _companyUiState = MutableStateFlow<CompanyUiState>(LoadingState.Idle)
    val companyUiState = _companyUiState.asStateFlow()

    val currentUiState: AddEditSeasonUiState
        get() = uiState.value

    private var checkCompanyCodeJob: Job? = null

    init {
        farmIdStream.value = farmId
        observeCompanyCode()
    }

    private fun observeCompanyCode() {
        viewModelScope.launch {
            uiState.map { it.companyCode }
                .distinctUntilChanged()
                .collectLatest { companyCode ->
                    if (companyCode != null && companyCode.length == 6) {
                        checkCompanyCode(companyCode)
                    } else {
                        resetCompany()
                    }
                }
        }
    }

    private fun checkCompanyCode(code: String) {
        resetCompanyCodeError()
        checkCompanyCodeJob?.cancel()
        _companyUiState.value = LoadingState.Loading

        checkCompanyCodeJob = viewModelScope.launch {
            runCancellableCatching {
                getContractFarmingCompanyByCode(code)
            }.onSuccess {
                val uiCompany = UiContractFarmingCompany.fromDomain(it)
                updateCompany(uiCompany)
                _companyUiState.value = LoadingState.Success(uiCompany)
            }.onFailure {
                _companyUiState.value = LoadingState.Error(it.errorText())
            }
        }
    }

    fun handleEvent(event: AddEditSeasonEvent) {
        when (event) {
            is AddEditSeasonEvent.OnEditFarmMeasurementCheckChanged -> {
                updateFarmMeasurementEditCheck(event.checked)
            }
            is AddEditSeasonEvent.OnFarmAreaChanged -> {
                updateFarmArea(event.area)
            }
            is AddEditSeasonEvent.OnFarmAreaMeasurementChanged -> {
                updateFarmAreaMeasurement(event.measurement)
            }
            is AddEditSeasonEvent.OnSeasonNameChanged -> {
                updateSeasonName(event.name)
            }
            is AddEditSeasonEvent.OnSeasonStartDateChanged -> {
                updateSeasonStartDate(event.date)
            }
            is AddEditSeasonEvent.OnFishListChanged -> {
                updateFishList(event.fishes)
            }
            is AddEditSeasonEvent.OnFishAdded -> {
                addFish(event.fish)
            }
            is AddEditSeasonEvent.OnFishRemoved -> {
                removeFish(event.fish)
            }
            is AddEditSeasonEvent.OnCompanyCodeChanged -> {
                updateCompanyCode(event.code)
            }
            is AddEditSeasonEvent.OnInputLoanInformationCheckChanged -> {
                updateLoanInformationInputCheck(event.checked)
            }
            is AddEditSeasonEvent.OnLoanAmountChanged -> {
                updateLoanAmount(event.amount)
            }
            is AddEditSeasonEvent.OnLoanDurationChanged -> {
                updateLoanDuration(event.duration)
            }
            is AddEditSeasonEvent.OnLoanOrganizationChanged -> {
                updateLoanOrganization(event.organization)
            }
            is AddEditSeasonEvent.OnLoanRemarkChanged -> {
                updateLoanRemark(event.remark)
            }
            AddEditSeasonEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditSeasonEvent.OnSeasonUploadingErrorShown -> {
                clearSeasonUploadingError()
            }
        }
    }

    private fun onFarmMeasurementLoaded(measurement: FarmMeasurement) {
        _uiState.update {
            it.copy(
                farmArea = measurement.area.value.toString(),
                farmMeasurement = when (measurement.measuredType) {
                    AreaMeasureMethod.Pin -> {
                        AreaMeasurement.Location(
                            latLng = measurement.location ?: LatLng(0.0, 0.0),
                            measurementType = AreaMeasureMethod.Pin
                        )
                    }
                    AreaMeasureMethod.Draw -> {
                        AreaMeasurement.Area(
                            coordinates = measurement.coordinates.orEmpty(),
                            acre = measurement.measuredArea?.value ?: 0.0,
                            measurementType = AreaMeasureMethod.Draw
                        )
                    }
                    AreaMeasureMethod.Walk -> {
                        AreaMeasurement.Area(
                            coordinates = measurement.coordinates.orEmpty(),
                            acre = measurement.measuredArea?.value ?: 0.0,
                            measurementType = AreaMeasureMethod.Walk
                        )
                    }
                    else -> {
                        null
                    }
                }
            )
        }
    }

    private fun updateFarmMeasurementEditCheck(checked: Boolean) {
        _uiState.update {
            it.copy(editFarmMeasurement = checked)
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
                delay(400)
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

    private fun updateSeasonName(name: String?) {
        _uiState.update {
            it.copy(seasonName = name)
        }
    }

    private fun updateSeasonStartDate(date: LocalDate) {
        _uiState.update {
            it.copy(seasonStartDate = date)
        }
    }

    private fun updateFishList(fishes: List<UiFish>) {
        _uiState.update {
            it.copy(fishes = fishes)
        }
    }

    private fun addFish(fish: UiFish) {
        val newFishList = mutableListOf<UiFish>()
        newFishList.addAll(currentUiState.fishes)
        newFishList.add(fish)
        updateFishList(newFishList)
    }

    private fun removeFish(fish: UiFish) {
        updateFishList(currentUiState.fishes.filterNot { it.id == fish.id })
    }

    private fun updateCompanyCode(code: String?) {
        _uiState.update {
            it.copy(companyCode = code)
        }
    }

    private fun updateCompany(company: UiContractFarmingCompany) {
        _uiState.update {
            it.copy(company = company)
        }
    }

    private fun resetCompany() {
        _uiState.update {
            it.copy(company = null)
        }
    }

    private fun resetCompanyCodeError() {
        _uiState.update {
            it.copy(companyCodeError = null)
        }
    }

    private fun updateLoanInformationInputCheck(checked: Boolean) {
        _uiState.update {
            it.copy(inputLoanInformation = checked)
        }
    }

    private fun updateLoanAmount(amount: String?) {
        _uiState.update {
            it.copy(loanAmount = amount)
        }
    }

    private fun updateLoanDuration(duration: UiLoanDuration?) {
        if (duration == currentUiState.loanDuration) return

        _uiState.update {
            it.copy(loanDuration = duration)
        }
    }

    private fun updateLoanOrganization(organization: String?) {
        _uiState.update {
            it.copy(loanOrganization = organization)
        }
    }

    private fun updateLoanRemark(remark: String?) {
        _uiState.update {
            it.copy(loanRemark = remark)
        }
    }

    private fun clearSeasonUploadingError() {
        _seasonUploadingUiState.value = LoadingState.Idle
    }

    private fun onSubmit() {
        // validate inputs
        val areaValidationResult = validateFarmArea(currentUiState.farmArea)
        val seasonNameValidationResult = validateSeasonName(currentUiState.seasonName)
        val fishesValidationResult = validateFishes(currentUiState.fishes)
        val companyCodeValidateResult =
            validateCompany(currentUiState.companyCode, currentUiState.company)
        val loanAmountValidationResult =
            validateLoanAmount(currentUiState.loanAmount, currentUiState.inputLoanInformation)
        val loanDurationValidationResult =
            validateLoanDuration(currentUiState.loanDuration, currentUiState.inputLoanInformation)
        val loanOrganizationValidationResult = validateLoanOrganization(
            currentUiState.loanOrganization,
            currentUiState.inputLoanInformation
        )

        // set/reset error
        _uiState.value = currentUiState.copy(
            farmAreaError = areaValidationResult.getErrorOrNull(),
            seasonNameError = seasonNameValidationResult.getErrorOrNull(),
            fishesError = fishesValidationResult.getErrorOrNull(),
            companyCodeError = companyCodeValidateResult.getErrorOrNull(),
            loanAmountError = loanAmountValidationResult.getErrorOrNull(),
            loanDurationError = loanDurationValidationResult.getErrorOrNull(),
            loanOrganizationError = loanOrganizationValidationResult.getErrorOrNull()
        )

        // return if there is any error
        if (hasError(
                areaValidationResult,
                seasonNameValidationResult,
                fishesValidationResult,
                companyCodeValidateResult,
                loanAmountValidationResult,
                loanDurationValidationResult,
                loanOrganizationValidationResult,
            )
        ) {
            return
        }

        // collect result
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
        val seasonName = seasonNameValidationResult.getDataOrThrow()
        val seasonStartDate = currentUiState.seasonStartDate
        val fishes = fishesValidationResult.getDataOrThrow()
        val company = companyCodeValidateResult.getDataOrNull()
        val loan = if (currentUiState.inputLoanInformation) {
            Loan(
                amount = loanAmountValidationResult.getDataOrThrow().orZero(),
                duration = loanDurationValidationResult.getDataOrThrow()?.month ?: 0,
                organization = loanOrganizationValidationResult.getDataOrNull().orEmpty(),
                remark = currentUiState.loanRemark
            )
        } else {
            null
        }
        val loanAmount = loanAmountValidationResult.getDataOrNull()
        val loanDuration = loanDurationValidationResult.getDataOrNull()
        val loanOrganization = loanOrganizationValidationResult.getDataOrNull()
        val loanRemark = currentUiState.loanRemark

        Timber.d("Area: $area")
        Timber.d("Location: $location")
        Timber.d("Coordinates: $coordinates")
        Timber.d("Measured area: $measuredArea")
        Timber.d("SeasonName: $seasonName")
        Timber.d("SeasonStartDate: $seasonStartDate")
        Timber.d("Fishes: $fishes")
        Timber.d("Company: $company")
        Timber.d("loanAmount: $loanAmount")
        Timber.d("loanDuration: $loanDuration")
        Timber.d("loanOrganization: $loanOrganization")
        Timber.d("loanRemark: $loanRemark")

        val request = SaveSeasonUseCase.SaveSeasonRequest(
            farmId = farmId,
            id = null, // TODO: change when support edit
            farmMeasurement = FarmMeasurement(
                location = location,
                coordinates = coordinates,
                area = area.asDomain(),
                measuredArea = Area.acreOrNull(measuredArea),
                measuredType = measureMethod,
                depth = farmMeasurementUiState.value.getDepthOrNull()
            ),
            seasonName = seasonName,
            seasonStartDate = seasonStartDate.toKotlinInstant(),
            fishes = fishes.map(UiFish::asDomainModel),
            company = company?.asDomainModel(),
            loan = loan
        )
        saveSeason(request)
    }

    private fun saveSeason(request: SaveSeasonUseCase.SaveSeasonRequest) {
        viewModelScope.launch {
            runCancellableCatching {
                _seasonUploadingUiState.value = LoadingState.Loading
                saveSeasonUseCase(request)
            }.onSuccess { result ->
                _seasonUploadingUiState.value = LoadingState.Success(Unit)
                _uiState.update {
                    it.copy(
                        addEditSeasonResult = AddEditSeasonUiState.AddEditSeasonResult(
                            seasonId = result.id
                        )
                    )
                }
            }.onFailure {
                _seasonUploadingUiState.value = LoadingState.Error(it.errorText())
            }
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

    private fun validateSeasonName(name: String?): ValidationResult<String> {
        return if (name.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(name)
        }
    }

    private fun validateFishes(fishes: List<UiFish>?): ValidationResult<List<UiFish>> {
        return if (fishes.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(fishes)
        }
    }

    private fun validateCompany(
        code: String?,
        company: UiContractFarmingCompany?
    ): ValidationResult<UiContractFarmingCompany?> {
        return if (!code.isNullOrEmpty() && company == null) {
            ValidationResult.Error(Text.ResourceText(R.string.ffr_add_edit_season_error_company_code))
        } else {
            ValidationResult.Success(company)
        }
    }

    private fun validateLoanAmount(
        amountString: String?,
        inputLoanChecked: Boolean
    ): ValidationResult<BigDecimal?> {
        val amount = amountString?.toBigDecimalOrNull()
        return if (inputLoanChecked && amount == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(amount)
        }
    }

    private fun validateLoanDuration(
        duration: UiLoanDuration?,
        inputLoanChecked: Boolean
    ): ValidationResult<UiLoanDuration?> {
        return if (inputLoanChecked && duration == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(duration)
        }
    }

    private fun validateLoanOrganization(
        organization: String?,
        inputLoanChecked: Boolean
    ): ValidationResult<String?> {
        return if (inputLoanChecked && organization.isNullOrEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success(organization)
        }
    }
}

private fun FarmMeasurementUiState.getDepthOrNull(): Double? {
    return (this as? LoadingState.Success)?.data?.depth
}

private fun farmMeasurementStream(
    farmId: String,
    getFarmMeasurementUseCase: GetFarmMeasurementUseCase,
    onFarmMeasurementLoaded: (FarmMeasurement) -> Unit
): Flow<LoadingState<UiFarmMeasurement>> {
    if (farmId.isEmpty()) return emptyFlow()

    return getFarmMeasurementUseCase(
        GetFarmMeasurementRequest(farmId)
    )
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data != null) {
                        onFarmMeasurementLoaded(result.data)
                        LoadingState.Success(UiFarmMeasurement.fromDomain(result.data))
                    } else {
                        LoadingState.Empty()
                    }
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