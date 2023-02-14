package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class UiSeason(
    val id: String,
    val name: String,
    val fishes: List<UiFish>? = null
) {
    companion object {
        fun fromDomain(domainModel: Season) = UiSeason(
            id = domainModel.id,
            name = domainModel.name,
            fishes = domainModel.fishes.map {
                UiFish.fromDomain(it)
            }
        )
    }
}