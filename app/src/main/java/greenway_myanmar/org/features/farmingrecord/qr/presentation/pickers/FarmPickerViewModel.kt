package greenway_myanmar.org.features.farmingrecord.qr.presentation.pickers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListPagingUseCase
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class FarmPickerViewModel @Inject constructor(
    getFarmListPagingUseCase: GetFarmListPagingUseCase
) : ViewModel() {

    private val _starterEvent = SingleLiveEvent<Any>()
    private val listing = _starterEvent.map { getFarmListPagingUseCase() }

    val farms = listing.switchMap { it.pagedList }
    val networkState = listing.switchMap { it.networkState }
    val refreshState = listing.switchMap { it.refreshState }

    init {
        _starterEvent.call()
    }
}