package greenway_myanmar.org.features.farmingrecord.qr.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddEditFarmingRecordQrViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFarmingRecordQrUiState())
    val uiState = _uiState.asStateFlow()

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
    val currentPageIndex: Int = AddEditQrPagerAdapter.FORM_PAGE_INDEX,
    val totalPage: Int = AddEditQrPagerAdapter.TOTAL_PAGE,
) {
    val currentProgress = currentPageIndex + 1
}

sealed class AddEditFarmingRecordQrEvent {
    data class PageChanged(val pageIndex: Int) : AddEditFarmingRecordQrEvent()
}