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
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrOrderUseCase.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.CreateQrUseCase.CreateQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmActivitiesUseCase.GetFarmActivitiesResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListUseCase.GetFarmListResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase.GetSeasonListResult
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason
import greenway_myanmar.org.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditFarmingRecordQrViewModel @Inject constructor(
    private val getFarmListUseCase: GetFarmListUseCase,
    private val getSeasonListUseCase: GetSeasonListUseCase,
    private val getFarmActivitiesUseCase: GetFarmActivitiesUseCase,
    private val createQrUseCase: CreateQrUseCase,
    private val createQrOrderUseCase: CreateQrOrderUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmingRecordQrUiState())
    val uiState = _uiState.asStateFlow()

    private val seasonId = MutableStateFlow<String>("")

    @Suppress("UNCHECKED_CAST")
    private val farmActivities: StateFlow<Resource<List<QrFarmActivity>>> =
        seasonId.transformLatest {
            if (it.isNotEmpty()) {
                emit(Resource.loading(null))
                when (val result = getFarmActivitiesUseCase()) {
                    is GetFarmActivitiesResult.Success -> {
                        emit(Resource.success(result.data))
                    }
                    is GetFarmActivitiesResult.Error -> {
                        emit(Resource.error(error = result.error))
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.loading(null)
        )

    private var loadFarmListJob: Job? = null
    private var loadSeasonListJob: Job? = null
    private var createQrJob: Job? = null
    private var createQrOrderJob: Job? = null

    init {
        loadFarmList()
        observeFarmChanged()
        collectFarmActivities()
    }

    private fun observeFarmChanged() {
        viewModelScope.launch {
            uiState.map { it.farm }
                .distinctUntilChanged()
                .collect {
                    resetSeasonUiState()
                    if (it != null) {
                        loadSeasonList(it, true)
                    }
                }
        }
    }

    private fun collectFarmActivities() {
        viewModelScope.launch {
            farmActivities.collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(farmActivities = resource.data.orEmpty().map {
                                QrFarmActivityItemUiState(
                                    activityName = it.activityName,
                                    date = it.date,
                                    farmInputs = it.farmInputs.orEmpty()
                                )
                            })
                        }
                    }
                    Status.ERROR -> {

                    }
                }

            }
        }
    }

    private fun resetSeasonUiState() {
        _uiState.update {
            it.copy(season = null, seasonList = null, showSeasonDropdown = false)
        }
    }

    private fun loadFarmList() {
        viewModelScope.launch {
            loadFarmListJob?.cancel()
            loadFarmListJob = launch {
                _uiState.update { currentUiState ->
                    currentUiState.copy(farmList = Resource.loading(null))
                }
                when (val result = getFarmListUseCase()) {
                    is GetFarmListResult.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                farmList = Resource.success(result.data.map {
                                    UiFarm.fromDomain(
                                        it
                                    )
                                }),
                                showFarmDropdown = uiState.value.farm == null
                            )
                        }
                    }
                    is GetFarmListResult.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(farmList = Resource.error(error = result.error))
                        }
                    }
                }
            }
        }
    }

    private fun retryLoadFarms() {
        loadFarmList()
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

    fun handleEvent(event: AddEditFarmingRecordQrEvent) {
        when (event) {
            is AddEditFarmingRecordQrEvent.LoadFarms -> {
                retryLoadFarms()
            }
            is AddEditFarmingRecordQrEvent.LoadSeasons -> {
                retryLoadSeasons()
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
        }
    }

    private fun updateCreateQrErrorShown() {
        _uiState.update {
            it.copy(createQrError = null)
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
            it.copy(optInShowPhone = show)
        }
    }

    private fun updateQuantity(quantityString: String) {
        val quantity = quantityString.toIntOrNull() ?: 0
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

        val hasError = listOf(
            farmResult,
            seasonResult,
            farmLocationTypeResult
        ).any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    farmError = farmResult.errorMessage(),
                    seasonError = seasonResult.errorMessage(),
                    farmLocationTypeError = farmLocationTypeResult.errorMessage()
                )
            }
            return
        }

        val farm = uiState.value.farm ?: return
        val season = uiState.value.season ?: return
        val farmLocationType = uiState.value.farmLocationType ?: return
        val optInShowPhone = uiState.value.optInShowPhone
        val optInShowFarmInput = uiState.value.optInShowFarmInput
        val optInShowYield = uiState.value.optInShowYield

        _uiState.update {
            it.copy(createQrLoading = true)
        }

        viewModelScope.launch {
            createQrJob?.cancel()
            createQrJob = launch {
                val result = createQrUseCase(
                    CreateQrUseCase.Param(
                        farmId = farm.id,
                        seasonId = season.id,
                        farmLocationType = farmLocationType.toDomain(),
                        optInShowPhone = optInShowPhone,
                        optInShowFarmInput = optInShowFarmInput,
                        optionShowYield = optInShowYield
                    )
                )

                when (result) {
                    is CreateQrResult.Success -> {
                        _uiState.update {
                            it.copy(
                                qrId = result.qrId,
                                currentPageIndex = getCurrentPageIndex() + 1,
                                createQrLoading = false
                            )
                        }
                    }
                    is CreateQrResult.Error -> {
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

        val quantity = uiState.value.quantity

        _uiState.update {
            it.copy(createQrOrderLoading = true)
        }

        viewModelScope.launch {
            createQrOrderJob?.cancel()
            createQrOrderJob = launch {
                val result = createQrOrderUseCase(
                    Param(
                        qrId = qrId,
                        quantity = quantity
                    )
                )

                when (result) {
                    CreateQrOrderResult.Success -> {
                        _uiState.update {
                            it.copy(
                                showOrderSuccess = true,
                                createQrOrderLoading = false
                            )
                        }
                    }
                    is CreateQrOrderResult.Error -> {
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
                farmLocationTypeError = null
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
        return if (uiState.value.quantity <= 0) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success
        }
    }

    fun getCurrentPageIndex() = uiState.value.currentPageIndex
    fun getQuantity(): Int = uiState.value.quantity
}

data class AddEditFarmingRecordQrUiState(
    val qrId: String? = null,
    val quantity: Int = 0,
    val quantityError: Text? = null,
    val farm: UiFarm? = null,
    val farmError: Text? = null,
    val season: UiSeason? = null,
    val seasonError: Text? = null,
    val farmLocationType: UiFarmLocationType? = null,
    val farmLocationTypeError: Text? = null,
    val optInShowPhone: Boolean = false,
    val optInShowFarmInput: Boolean = false,
    val optInShowYield: Boolean = false,
    val farmList: Resource<List<UiFarm>>? = null,
    val seasonList: Resource<List<UiSeason>>? = null,
    val showFarmDropdown: Boolean = false,
    val showSeasonDropdown: Boolean = false,
    val currentPageIndex: Int = AddEditQrPagerAdapter.FORM_PAGE_INDEX,
    val totalPage: Int = AddEditQrPagerAdapter.TOTAL_PAGE,
    val farmActivities: List<QrFarmActivityItemUiState> = emptyList(),
    val createQrError: Text? = null,
    val createQrLoading: Boolean = false,
    val createQrOrderError: Text? = null,
    val createQrOrderLoading: Boolean = false,
    val showOrderSuccess: Boolean = false
) {
    val currentProgress = currentPageIndex + 1
    val loading = createQrLoading || createQrOrderLoading
}

sealed class AddEditFarmingRecordQrEvent {
    data class FarmChanged(val farm: UiFarm) : AddEditFarmingRecordQrEvent()
    data class SeasonChanged(val seasons: UiSeason) : AddEditFarmingRecordQrEvent()
    data class FarmLocationTypeChanged(val type: UiFarmLocationType?) :
        AddEditFarmingRecordQrEvent()

    object LoadSeasons : AddEditFarmingRecordQrEvent()
    object LoadFarms : AddEditFarmingRecordQrEvent()
    data class OptInShowPhoneChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class OptInShowFarmInputChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class OptInShowYieldChanged(val show: Boolean) : AddEditFarmingRecordQrEvent()
    data class PageChanged(val pageIndex: Int) : AddEditFarmingRecordQrEvent()
    class QuantityChanged(val quantity: String) : AddEditFarmingRecordQrEvent()

    object Submit : AddEditFarmingRecordQrEvent()
    object ConfirmOrder : AddEditFarmingRecordQrEvent()
    object CreateQrErrorShown : AddEditFarmingRecordQrEvent()
    object OrderSuccessShown : AddEditFarmingRecordQrEvent()
}