package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Resource
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.ValidationResult
import greenway_myanmar.org.common.domain.entities.errorMessage
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateUpdateQrUseCase.CreateUpdateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrDetailUseCase.GetQrDetailResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrDetailUseCase.Param
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrLifetimeListUseCase.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetQrQuantityListUseCase.GetQrQuantityListResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase.GetSeasonListResult
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditFarmingRecordQrViewModel @Inject constructor(
    private val getSeasonListUseCase: GetSeasonListUseCase,
    private val createUpdateQrUseCase: CreateUpdateQrUseCase,
    private val createQrOrderUseCase: CreateQrOrderUseCase,
    private val getQrQuantityListUseCase: GetQrQuantityListUseCase,
    private val getQrLifetimeListUseCase: GetQrLifetimeListUseCase,
    private val getQrDetailUseCase: GetQrDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmingRecordQrUiState())
    val uiState = _uiState.asStateFlow()

    private val seasonId = MutableStateFlow<String>("")

    private var loadFarmListJob: Job? = null
    private var loadSeasonListJob: Job? = null
    private var loadQuantityListJob: Job? = null
    private var loadQrLifetimeListJob: Job? = null
    private var loadQrDetailJob: Job? = null
    private var createUpdateQrJob: Job? = null
    private var createQrOrderJob: Job? = null

    init {
        observeFarmChanged()
        observeQrIdChanged()
        loadQrLifetimes(false)
        loadQuantityList(false)
    }

    private fun observeFarmChanged() {
        viewModelScope.launch {
            uiState.map { it.farm }
                .distinctUntilChanged()
                .collect {
                    resetSeasonUiState()
                    if (it != null) {
                        loadSeasonList(it, uiState.value.showSeasonDropdown ?: true)
                    }
                }
        }
    }

    private fun observeQrIdChanged() {
        viewModelScope.launch {
            uiState.map { it.qrId }
                .distinctUntilChanged()
                .collect {
                    if (it.isNullOrEmpty()) {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(qrDetail = null)
                        }
                    } else {
                        loadQrDetail(it)
                    }
                }
        }
    }

    private fun resetSeasonUiState() {
        _uiState.update {
            it.copy(
                season = null,
                seasonList = null,
                showSeasonDropdown = null
            )
        }
    }

    private fun retryLoadSeasons() {
        val farm = uiState.value.farm
        if (farm != null) {
            loadSeasonList(farm, true)
        }
    }

    private fun loadSeasonList(farm: UiFarm, showSeasonDropdown: Boolean = false) {
        loadSeasonListJob?.cancel()
        viewModelScope.launch {
            loadSeasonListJob = launch {
                runCancellableCatching {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(seasonList = Resource.loading(null))
                    }
                    getSeasonListUseCase(GetSeasonListUseCase.Param(farm.id))
                }.onSuccess { result ->
                    when (result) {
                        is GetSeasonListResult.Success -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(
                                    seasonList = Resource.success(result.data.map {
                                        UiSeason.fromDomain(
                                            it
                                        )
                                    }),
                                    showSeasonDropdown = showSeasonDropdown
                                )
                            }
                        }
                        is GetSeasonListResult.Error -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(seasonList = Resource.error(error = result.error))
                            }
                        }
                    }
                }.onFailure {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(seasonList = Resource.from(it))
                    }
                }
            }
        }
    }

    private fun loadQuantityList(showDropdown: Boolean = false) {
        loadQuantityListJob?.cancel()
        viewModelScope.launch {
            loadQuantityListJob = launch {
                runCancellableCatching {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(quantityList = Resource.loading(null))
                    }
                    getQrQuantityListUseCase()
                }.onSuccess { result ->
                    when (result) {
                        is GetQrQuantityListResult.Success -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(
                                    quantityList = Resource.success(result.prices.map {
                                        UiQrQuantity.fromDomain(
                                            it
                                        )
                                    }),
                                    showQuantityDropdown = showDropdown
                                )
                            }
                        }
                        is GetQrQuantityListResult.Error -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(quantityList = Resource.error(error = result.error))
                            }
                        }
                    }
                }.onFailure {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(quantityList = Resource.from(it))
                    }
                }
            }
        }
    }

    private fun loadQrLifetimes(showDropdown: Boolean = false) {
        loadQrLifetimeListJob?.cancel()
        viewModelScope.launch {
            loadQrLifetimeListJob = launch {
                runCancellableCatching {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(quantityList = Resource.loading(null))
                    }
                    getQrLifetimeListUseCase()
                }.onSuccess { result ->
                    when (result) {
                        is GetQrLifetimeListResult.Success -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(
                                    qrLifetimeList = Resource.success(result.lifetimes.map {
                                        UiQrLifetime.fromDomain(
                                            it
                                        )
                                    }),
                                    showQuantityDropdown = showDropdown
                                )
                            }
                        }
                        is GetQrLifetimeListResult.Error -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(qrLifetimeList = Resource.error(error = result.error))
                            }
                        }
                    }
                }.onFailure {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(qrLifetimeList = Resource.from(it))
                    }
                }
            }
        }
    }

    private fun loadQrDetail(qrId: String) {
        loadQrDetailJob?.cancel()
        viewModelScope.launch {
            loadQrDetailJob = launch {
                runCancellableCatching {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(qrDetail = Resource.loading(null))
                    }
                    getQrDetailUseCase(Param(qrId))
                }.onSuccess { result ->
                    when (result) {
                        is GetQrDetailResult.Success -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(
                                    qrDetail = Resource.success(
                                        UiQrDetail.fromDomain(
                                            result.data
                                        )
                                    )
                                )
                            }
                        }
                        is GetQrDetailResult.Error -> {
                            _uiState.update { currentUiState ->
                                currentUiState.copy(qrDetail = Resource.error(error = result.error))
                            }
                        }
                    }
                }.onFailure {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(qrDetail = Resource.from(it))
                    }
                }
            }
        }
    }

    fun handleEvent(event: AddEditFarmingRecordQrEvent) {
        when (event) {
            is AddEditFarmingRecordQrEvent.LoadSeasons -> {
                retryLoadSeasons()
            }
            is AddEditFarmingRecordQrEvent.LoadQuantities -> {
                loadQuantityList(true)
            }
            is AddEditFarmingRecordQrEvent.LoadQrLifetimes -> {
                loadQrLifetimes(true)
            }
            is AddEditFarmingRecordQrEvent.PageChanged -> {
                updatePage(event.pageIndex)
            }
            is AddEditFarmingRecordQrEvent.FarmChanged -> {
                updateFarm(event.farm)
            }
            is AddEditFarmingRecordQrEvent.SeasonChanged -> {
                updateSeason(event.seasons)
            }
            is AddEditFarmingRecordQrEvent.FarmLocationTypeChanged -> {
                updateFarmLocationType(event.type)
            }
            is AddEditFarmingRecordQrEvent.OptInShowPhoneChanged -> {
                updateOptInShowPhone(event.show)
            }
            is AddEditFarmingRecordQrEvent.OptInShowFarmInputChanged -> {
                updateOptInShowFarmInput(event.show)
            }
            is AddEditFarmingRecordQrEvent.OptInShowYieldChanged -> {
                updateOptInShowYield(event.show)
            }
            is AddEditFarmingRecordQrEvent.CreateQrErrorShown -> {
                updateCreateQrErrorShown()
            }
            is AddEditFarmingRecordQrEvent.SeasonDropdownShown -> {
                markSeasonDropdownShown()
            }
            is AddEditFarmingRecordQrEvent.Submit -> {
                onSubmit()
            }
            is AddEditFarmingRecordQrEvent.ConfirmOrder -> {
                onConfirmOrder()
            }
            is AddEditFarmingRecordQrEvent.OrderSuccessShown -> {
                orderSuccessShown()
            }
            is AddEditFarmingRecordQrEvent.QuantityChanged -> {
                updateQuantity(event.quantity)
            }
            is AddEditFarmingRecordQrEvent.QrLifetimeChanged -> {
                updateQrLifetime(event.lifetime)
            }
            is AddEditFarmingRecordQrEvent.PhoneInputShown -> {
                markPhoneInputShown()
            }
            is AddEditFarmingRecordQrEvent.PhoneChanged -> {
                updatePhone(event.phone)
            }
            AddEditFarmingRecordQrEvent.SeasonDropdownShown -> {
                markSeasonDropdownShown()
            }
        }
    }

    private fun updateQrLifetime(lifetime: UiQrLifetime) {
        _uiState.update {
            it.copy(qrLifetime = lifetime)
        }
    }

    private fun markPhoneInputShown() {
        _uiState.update {
            it.copy(showPhoneInput = false)
        }
    }

    private fun updatePhone(phone: String) {
        val oldValue = uiState.value.phone
        if (oldValue == phone) {
            return
        }
        _uiState.update {
            it.copy(
                phone = phone
            )
        }
    }

    private fun updateCreateQrErrorShown() {
        _uiState.update {
            it.copy(createQrError = null)
        }
    }

    private fun markSeasonDropdownShown() {
        _uiState.update {
            it.copy(showSeasonDropdown = false)
        }
    }

    private fun updatePage(pageIndex: Int) {
        if (pageIndex == _uiState.value.currentPageIndex) {
            return
        }

        _uiState.update {
            it.copy(currentPageIndex = pageIndex)
        }
    }

    private fun updateFarm(farm: UiFarm) {
        _uiState.update {
            it.copy(farm = farm)
        }
    }

    private fun updateSeason(season: UiSeason) {
        _uiState.update {
            it.copy(season = season)
        }
    }

    private fun updateFarmLocationType(type: UiFarmLocationType?) {
        _uiState.update {
            it.copy(farmLocationType = type)
        }
    }

    private fun updateOptInShowPhone(show: Boolean) {
        _uiState.update {
            it.copy(
                optInShowPhone = show
            )
        }
    }

    private fun updateQuantity(quantity: UiQrQuantity) {
        _uiState.update {
            it.copy(quantity = quantity)
        }
    }

    private fun updateOptInShowFarmInput(show: Boolean) {
        _uiState.update {
            it.copy(optInShowFarmInput = show)
        }
    }

    private fun updateOptInShowYield(show: Boolean) {
        _uiState.update {
            it.copy(optInShowYield = show)
        }
    }

    private fun onSubmit() {
        resetValidationErrors()
        val farmResult = validateFarm()
        val seasonResult = validateSeason()
        val farmLocationTypeResult = validateFarmLocationType()
        val qrLifetimeResult = validateQrLifetime()
        val phoneResult = validatePhone()

        val hasError = listOf(
            farmResult,
            seasonResult,
            farmLocationTypeResult,
            qrLifetimeResult
        ).any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    farmError = farmResult.errorMessage(),
                    seasonError = seasonResult.errorMessage(),
                    farmLocationTypeError = farmLocationTypeResult.errorMessage(),
                    qrLifetimeError = qrLifetimeResult.errorMessage(),
                    phoneError = phoneResult.errorMessage()
                )
            }
            return
        }

        val farm = uiState.value.farm ?: return
        val season = uiState.value.season ?: return
        val farmLocationType = uiState.value.farmLocationType ?: return
        val optInShowPhone = uiState.value.optInShowPhone
        val phone = uiState.value.phone
        val optInShowFarmInput = uiState.value.optInShowFarmInput
        val optInShowYield = uiState.value.optInShowYield
        val qrLifetime = uiState.value.qrLifetime ?: return

        if (uiState.value.isInEditMode && uiState.value.qrId.isNullOrEmpty()) {
            return
        }

        _uiState.update {
            it.copy(createQrLoading = true)
        }

        viewModelScope.launch {
            createUpdateQrJob?.cancel()
            createUpdateQrJob = launch {
                val result = createUpdateQrUseCase(
                    CreateUpdateQrUseCase.Param(
                        qrId = uiState.value.qrId,
                        farmId = farm.id,
                        seasonId = season.id,
                        farmLocationType = farmLocationType.toDomain(),
                        optInShowPhone = optInShowPhone,
                        optInShowFarmInput = optInShowFarmInput,
                        optionShowYield = optInShowYield,
                        qrLifetime = qrLifetime.month,
                        phone = phone
                    )
                )

                when (result) {
                    is CreateUpdateQrResult.Success -> {
                        _uiState.update {
                            it.copy(
                                qrId = result.qrId,
                                currentPageIndex = getCurrentPageIndex() + 1,
                                createQrLoading = false
                            )
                        }
                    }
                    is CreateUpdateQrResult.Error -> {
                        _uiState.update {
                            it.copy(createQrError = result.error.error, createQrLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun onConfirmOrder() {
        resetValidationErrors()
        val qrId = uiState.value.qrId
        if (qrId.isNullOrEmpty()) return

        resetValidationErrors()
        val quantityResult = validateQuantity()

        val hasError = listOf(quantityResult).any { it is ValidationResult.Error }
        if (hasError) {
            _uiState.update {
                it.copy(
                    quantityError = quantityResult.errorMessage()
                )
            }
            return
        }

        val quantity = uiState.value.quantity?.quantity ?: return
        val qrUrl = uiState.value.qrDetail?.data?.qrUrl ?: return
        val qrIdNumber = uiState.value.qrDetail?.data?.qrIdNumber ?: return

        _uiState.update {
            it.copy(createQrOrderLoading = true)
        }

        viewModelScope.launch {
            createQrOrderJob?.cancel()
            createQrOrderJob = launch {
                val result = createQrOrderUseCase(
                    CreateQrOrderUseCase.Param(
                        qrId = qrId,
                        quantity = quantity,
                        qrUrl = qrUrl,
                        qrIdNumber = qrIdNumber
                    )
                )

                when (result) {
                    CreateQrOrderUseCase.CreateQrOrderResult.Success -> {
                        _uiState.update {
                            it.copy(
                                showOrderSuccess = true,
                                refreshQrList = true,
                                createQrOrderLoading = false
                            )
                        }
                    }
                    is CreateQrOrderUseCase.CreateQrOrderResult.Error -> {
                        _uiState.update {
                            it.copy(
                                createQrOrderError = result.error.error,
                                createQrOrderLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun orderSuccessShown() {
        _uiState.update {
            it.copy(showOrderSuccess = false)
        }
    }

    private fun resetValidationErrors() {
        _uiState.update {
            it.copy(
                farmError = null,
                seasonError = null,
                quantityError = null,
                farmLocationTypeError = null,
                qrLifetimeError = null,
                phoneError = null
            )
        }
    }

    private fun validateFarm(): ValidationResult {
        return if (uiState.value.farm == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    private fun validateSeason(): ValidationResult {
        return if (uiState.value.season == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    private fun validateFarmLocationType(): ValidationResult {
        return if (uiState.value.farmLocationType == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    private fun validateQuantity(): ValidationResult {
        return if (uiState.value.quantity == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    private fun validateQrLifetime(): ValidationResult {
        return if (uiState.value.qrLifetime == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    private fun validatePhone(): ValidationResult {
        return if (uiState.value.optInShowPhone && uiState.value.phone.isEmpty()) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    fun getCurrentPageIndex() = uiState.value.currentPageIndex
    fun getQuantity(): Int = uiState.value.quantity?.quantity ?: 0
    fun shouldExpandSeasonDropdown() = uiState.value.showSeasonDropdown ?: false
}

data class AddEditFarmingRecordQrUiState(
    val isInEditMode: Boolean = false,
    val qrId: String? = null,
    val quantity: UiQrQuantity? = null,
    val quantityError: Text? = null,
    val farm: UiFarm? = null,
    val farmError: Text? = null,
    val season: UiSeason? = null,
    val seasonError: Text? = null,
    val farmLocationType: UiFarmLocationType? = null,
    val farmLocationTypeError: Text? = null,
    val optInShowPhone: Boolean = false,
    val phone: String = "",
    val phoneError: Text? = null,
    val showPhoneInput: Boolean = false,
    val optInShowFarmInput: Boolean = false,
    val optInShowYield: Boolean = false,
    val qrLifetime: UiQrLifetime? = null,
    val qrLifetimeError: Text? = null,
    val seasonList: Resource<List<UiSeason>>? = null,
    val quantityList: Resource<List<UiQrQuantity>>? = null,
    val qrLifetimeList: Resource<List<UiQrLifetime>>? = null,
    val qrDetail: Resource<UiQrDetail>? = null,
    val showFarmDropdown: Boolean = false,
    val showSeasonDropdown: Boolean? = null,
    val showQuantityDropdown: Boolean = false,
    val currentPageIndex: Int = AddEditQrPagerAdapter.FORM_PAGE_INDEX,
    val totalPage: Int = AddEditQrPagerAdapter.TOTAL_PAGE,
    val createQrError: Text? = null,
    val createQrLoading: Boolean = false,
    val createQrOrderError: Text? = null,
    val createQrOrderLoading: Boolean = false,
    val qrInfoLoading: Boolean = false,
    val showOrderSuccess: Boolean = false,
    val refreshQrList: Boolean = false
) {
    val currentProgress = currentPageIndex + 1
    val loading = createQrLoading || createQrOrderLoading
    val estimatedPriceLabel: Text.ResourceFormattedText? =
        if (quantity != null) {
            Text.ResourceFormattedText(
                R.string.label_farming_record_qr_estimated_cost,
                listOf(quantity.formattedPrice)
            )
        } else {
            null
        }
    val showQrDetail: Boolean = quantity != null
}

sealed class AddEditFarmingRecordQrEvent {
    data class FarmChanged(val farm: UiFarm) : AddEditFarmingRecordQrEvent()
    data class SeasonChanged(val seasons: UiSeason) : AddEditFarmingRecordQrEvent()
    data class FarmLocationTypeChanged(val type: UiFarmLocationType?) :
        AddEditFarmingRecordQrEvent()

    object LoadSeasons : AddEditFarmingRecordQrEvent()
    object LoadQrLifetimes : AddEditFarmingRecordQrEvent()
    object LoadQuantities : AddEditFarmingRecordQrEvent()
    data class OptInShowPhoneChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class OptInShowFarmInputChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class OptInShowYieldChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class PageChanged(val pageIndex: Int) : AddEditFarmingRecordQrEvent()
    class QuantityChanged(val quantity: UiQrQuantity) : AddEditFarmingRecordQrEvent()
    data class QrLifetimeChanged(val lifetime: UiQrLifetime) : AddEditFarmingRecordQrEvent()
    object Submit : AddEditFarmingRecordQrEvent()
    object ConfirmOrder : AddEditFarmingRecordQrEvent()
    object CreateQrErrorShown : AddEditFarmingRecordQrEvent()
    object OrderSuccessShown : AddEditFarmingRecordQrEvent()
    object PhoneInputShown : AddEditFarmingRecordQrEvent()
    data class PhoneChanged(val phone: String) : AddEditFarmingRecordQrEvent()
    object SeasonDropdownShown : AddEditFarmingRecordQrEvent()
}