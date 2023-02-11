package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

sealed class OpeningSeasonEvent {
    data class OnSeasonIdChanged(val seasonId: String): OpeningSeasonEvent()

}
