package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail

sealed class FarmDetailEvent {
    data class OnSelectedTabChanged(val position: Int): FarmDetailEvent()
}