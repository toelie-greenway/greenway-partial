package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.utils.runCancellableCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GenerateNewQrUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GenerateNewQrUseCase.GenerateNewQrResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmActivitiesUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmActivitiesUseCase.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListUseCase.*
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase.GetSeasonListResult
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason
import greenway_myanmar.org.vo.Resource
import greenway_myanmar.org.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditFarmingRecordQrViewModel @Inject constructor(
    private val generateNewQrUseCase: GenerateNewQrUseCase,
    private val getFarmListUseCase: GetFarmListUseCase,
    private val getSeasonListUseCase: GetSeasonListUseCase,
    private val getFarmActivitiesUseCase: GetFarmActivitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmingRecordQrUiState())
    val uiState = _uiState.asStateFlow()

    private val seasonId = MutableStateFlow<String>("")
    @Suppress("UNCHECKED_CAST")
    private val farmActivities: StateFlow<Resource<List<QrFarmActivity>>> = seasonId.transformLatest {
        if (it.isNotEmpty()) {
            emit(Resource.loading(null))
            when (val result = getFarmActivitiesUseCase()) {
                is GetFarmActivitiesResult.Success -> {
                    emit(Resource.success(result.data))
                }
                is GetFarmActivitiesResult.Error -> {
                    emit(Resource.error(result.message))
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.loading(null)
    ) as StateFlow<Resource<List<QrFarmActivity>>>

    private var generateNewQrJob: Job? = null
    private var loadFarmListJob: Job? = null
    private var loadSeasonListJob: Job? = null

    init {
        generateNewQr()
        loadFarmList()
        observeFarmChanged()
        collectFarmActivities()

        seasonId.value = "3"
    }

    private fun observeFarmChanged() {
        viewModelScope.launch {
            uiState.map { it.farm }
                .distinctUntilChanged()
                .collect {
                    if (it == null) {
                        resetSeasonUiState()
                    } else {
                        loadSeasonList(it)
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
            it.copy(season = null, seasonList = null)
        }
    }

    private fun generateNewQr() {
        generateNewQrJob?.cancel()
        generateNewQrJob = viewModelScope.launch {
            runCancellableCatching {
                generateNewQrUseCase()
            }.onSuccess { result ->
                when (result) {
                    is GenerateNewQrResult.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(qrNumber = result.qrNumber, qrUrl = result.qrUrl)
                        }
                    }
                    is GenerateNewQrResult.Error -> {

                    }
                }
            }.onFailure {

            }
        }
    }

    private fun loadFarmList() {
        loadFarmListJob?.cancel()
        loadFarmListJob = viewModelScope.launch {
            runCancellableCatching {
                _uiState.update { currentUiState ->
                    currentUiState.copy(farmList = Resource.loading(null))
                }
                getFarmListUseCase()
            }.onSuccess { result ->
                when (result) {
                    is GetFarmListResult.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(farmList = Resource.success(result.data.map {
                                UiFarm.fromDomain(
                                    it
                                )
                            }))
                        }
                    }
                    is GetFarmListResult.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(farmList = Resource.error(result.message))
                        }
                    }
                }
            }.onFailure {
                _uiState.update { currentUiState ->
                    currentUiState.copy(farmList = Resource.from(it))
                }
            }
        }
    }

    private fun loadSeasonList(farm: UiFarm, showSeasonDropdown: Boolean = false) {
        loadSeasonListJob?.cancel()
        loadSeasonListJob = viewModelScope.launch {
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
                            currentUiState.copy(seasonList = Resource.error(result.message))
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

    fun handleEvent(event: AddEditFarmingRecordQrEvent) {
        when (event) {
            is AddEditFarmingRecordQrEvent.PageChanged -> {
                updatePage(event.pageIndex)
            }
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

    fun getCurrentPageIndex() = uiState.value.currentPageIndex
}

data class AddEditFarmingRecordQrUiState(
    val qrNumber: String = "",
    val qrUrl: String = "",
    val farm: UiFarm? = null,
    val season: UiSeason? = null,
    val farmList: Resource<List<UiFarm>>? = null,
    val seasonList: Resource<List<UiSeason>>? = null,
    val showSeasonDropdown: Boolean = false,
    val currentPageIndex: Int = AddEditQrPagerAdapter.FORM_PAGE_INDEX,
    val totalPage: Int = AddEditQrPagerAdapter.TOTAL_PAGE,
    val farmActivities: List<QrFarmActivityItemUiState> = emptyList()
) {
    val currentProgress = currentPageIndex + 1
}

sealed class AddEditFarmingRecordQrEvent {
    data class PageChanged(val pageIndex: Int) : AddEditFarmingRecordQrEvent()
}