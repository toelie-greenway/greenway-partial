package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import java.math.BigDecimal

data class UiSeason(
    val id: String,
    val name: String,
    val fishes: List<UiFish> = emptyList(),
    val isHarvested: Boolean,
    val totalExpenses: BigDecimal = BigDecimal.ZERO
) {
    companion object {
        fun fromDomain(domainModel: Season) = UiSeason(
            id = domainModel.id,
            name = domainModel.name,
            fishes = domainModel.fishes.map {
                UiFish.fromDomain(it)
            },
            isHarvested = domainModel.isHarvested,
            totalExpenses = domainModel.totalExpenses
        )
    }
}