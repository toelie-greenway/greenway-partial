package greenway_myanmar.org.features.fishfarmrecord.presentation.closedseasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Pond
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.ObserveClosedSeasonsUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.ObservePondUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.ObservePondUseCase.ObservePondRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SAVED_STATE_KEY_POND_ID = "keys.POND_ID"

@HiltViewModel
class ClosedSeasonsViewModel @Inject constructor(
    private val observePondUseCase: ObservePondUseCase,
    private val observeClosedSeasonsUseCase: ObserveClosedSeasonsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pondIdFlow = savedStateHandle.getStateFlow(SAVED_STATE_KEY_POND_ID, "")

    private val _uiState = MutableStateFlow<ClosedSeasonsUiState>(ClosedSeasonsUiState())
    val uiState = _uiState.asStateFlow()

    private var pondJob: Job? = null
    private var sJob: Job? = null

    init {
        viewModelScope.launch {
            pondIdFlow.collect { pondId ->
                if (pondId.isNotEmpty()) {
                    loadPond(pondId)
                }
            }
        }
        setPondId("2")
    }

    fun setPondId(pondId: String) {
        savedStateHandle[SAVED_STATE_KEY_POND_ID] = pondId
    }

    private fun loadPond(pondId: String) {
        pondJob?.cancel()
        pondJob = viewModelScope.launch {
            observePondUseCase(ObservePondRequest(pondId)).collect { result ->
                when (result) {
                    is Result.Success -> {
                        loadCloseSeasons(result.data)
                    }
                    is Result.Error -> {

                    }
                    Result.Loading -> {

                    }
                }
            }

        }
    }


    private fun loadCloseSeasons(pond: Pond) {
        viewModelScope.launch {
            observeClosedSeasonsUseCase(Unit).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(closedSeasons = result.data.map { item ->
                                ClosedSeasonListItemUiState.from(item, pond.name, pond.area)
                            })
                        }
                    }
                    is Result.Error -> {

                    }
                    Result.Loading -> {

                    }
                }
            }

        }
    }

}