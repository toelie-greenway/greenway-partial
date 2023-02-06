package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.GetFcrRecordsStreamUseCase
import greenway_myanmar.org.util.WhileViewSubscribed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FcrRecordListViewModel @Inject constructor(
    getFcrRecordsStreamUseCase: GetFcrRecordsStreamUseCase
) : ViewModel() {

    private val seasonIdStream = MutableStateFlow("")

    val recordsUiState: StateFlow<RecordsUiState> = seasonIdStream.flatMapLatest { seasonId ->
        fcrRecordsStream(seasonId, getFcrRecordsStreamUseCase)
    }.stateIn(viewModelScope, WhileViewSubscribed, LoadingState.Idle)

    init {

    }
}

private fun fcrRecordsStream(
    seasonId: String,
    getFcrRecordsStreamUseCase: GetFcrRecordsStreamUseCase
): Flow<RecordsUiState> {
    return getFcrRecordsStreamUseCase()
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    LoadingState.Success(result.data.map {
                        FcrRecordListItemUiState.fromDomainModel(it)
                    })
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