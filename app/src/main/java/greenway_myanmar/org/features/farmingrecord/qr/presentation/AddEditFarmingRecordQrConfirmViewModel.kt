package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmActivity
import javax.inject.Inject

@HiltViewModel
class AddEditFarmingRecordQrConfirmViewModel @Inject constructor(

) : ViewModel() {

//    private val _uiState = MutableStateFlow(AddEditFarmingRecordQrUiState())
//    val uiState = _uiState.asStateFlow()
//
//    @Suppress("UNCHECKED_CAST")
//    private val farmActivities: StateFlow<Resource<List<QrFarmActivity>>> =
//        seasonId.transformLatest {
//            if (it.isNotEmpty()) {
//                emit(Resource.loading(null))
//                when (val result = getFarmActivitiesUseCase()) {
//                    is GetFarmActivitiesUseCase.GetFarmActivitiesResult.Success -> {
//                        emit(Resource.success(result.data))
//                    }
//                    is GetFarmActivitiesUseCase.GetFarmActivitiesResult.Error -> {
//                        emit(Resource.error(error = result.error))
//                    }
//                }
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = Resource.loading(null)
//        )
//
//    private fun collectFarmActivities() {
//        viewModelScope.launch {
//            farmActivities.collect { resource ->
//                when (resource.status) {
//                    Status.LOADING -> {
//
//                    }
//                    Status.SUCCESS -> {
//                        _uiState.update { currentUiState ->
//                            currentUiState.copy(farmActivities = resource.data.orEmpty().map {
//                                QrFarmActivityItemUiState(
//                                    activityName = it.activityName,
//                                    date = it.date,
//                                    farmInputs = it.farmInputs.orEmpty()
//                                )
//                            })
//                        }
//                    }
//                    Status.ERROR -> {
//
//                    }
//                }
//
//            }
//        }
//    }

}

data class AddEditFarmingRecordQrConfirmUiState(
    val farmActivities: List<UiFarmActivity> = emptyList()
)