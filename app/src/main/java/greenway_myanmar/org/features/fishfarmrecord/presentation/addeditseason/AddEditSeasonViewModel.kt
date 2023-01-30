package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.domain.model.getErrorOrNull
import greenway_myanmar.org.features.fishfarmrecord.domain.model.hasError
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetContractFarmingCompanyByCode
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLoanDuration
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditSeasonViewModel @Inject constructor(
    private val getContractFarmingCompanyByCode: GetContractFarmingCompanyByCode
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSeasonUiState())
    val uiState = _uiState.asStateFlow()

    private val _companyUiState = MutableStateFlow<CompanyUiState>(LoadingState.Idle)
    val companyUiState = _companyUiState.asStateFlow()

    val currentUiState: AddEditSeasonUiState
        get() = uiState.value

    private var checkCompanyCodeJob: Job? = null

    init {
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
            is AddEditSeasonEvent.OnCompanyCodeChanged -> {
                updateCompanyCode(event.code)
            }
            is AddEditSeasonEvent.OnInputLoanInformationCheckChanged -> {
                toggleLoanInformationInputCheck()
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

    private fun toggleLoanInformationInputCheck() {
        _uiState.update {
            it.copy(inputLoanInformation = currentUiState.inputLoanInformation.not())
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

    private fun onSubmit() {
        // validate inputs
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
            seasonNameError = seasonNameValidationResult.getErrorOrNull(),
            fishesError = fishesValidationResult.getErrorOrNull(),
            companyCodeError = companyCodeValidateResult.getErrorOrNull(),
            loanAmountError = loanAmountValidationResult.getErrorOrNull(),
            loanDurationError = loanDurationValidationResult.getErrorOrNull(),
            loanOrganizationError = loanOrganizationValidationResult.getErrorOrNull()
        )

        // return if there is any error
        if (hasError(
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
        val seasonName = seasonNameValidationResult.getDataOrThrow()
        val seasonStartDate = currentUiState.seasonStartDate
        val fishes = fishesValidationResult.getDataOrThrow()
        val company = companyCodeValidateResult.getDataOrNull()
        val loanAmount = loanAmountValidationResult.getDataOrNull()
        val loanDuration = loanDurationValidationResult.getDataOrNull()
        val loanOrganization = loanOrganizationValidationResult.getDataOrNull()
        val loanRemark = currentUiState.loanRemark

        Timber.d("SeasonName: $seasonName")
        Timber.d("SeasonStartDate: $seasonStartDate")
        Timber.d("Fishes: $fishes")
        Timber.d("Company: $company")
        Timber.d("loanAmount: $loanAmount")
        Timber.d("loanDuration: $loanDuration")
        Timber.d("loanOrganization: $loanOrganization")
        Timber.d("loanRemark: $loanRemark")
//        _uiState.value = currentUiState.copy(
//            inputResult = InputResult(
//                seasonName = seasonName,
//            )
//        )
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