package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.list

import androidx.lifecycle.ViewModel
import com.greenwaymyanmar.core.presentation.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class FcrRecordListViewModel @Inject constructor(

) : ViewModel() {
    private val _recordsUiState = MutableStateFlow<RecordsUiState>(LoadingState.Idle)
    val recordsUiState = _recordsUiState.asStateFlow()

    init {
        _recordsUiState.value = LoadingState.Success(
            listOf(
                FcrRecordListItemUiState(
                    "1",
                    Clock.System.now(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
                ),
                FcrRecordListItemUiState(
                    "2",
                    Clock.System.now(),
                    BigDecimal.TEN,
                    BigDecimal.TEN,
                    BigDecimal.TEN
                ),
            )
        )
    }
}