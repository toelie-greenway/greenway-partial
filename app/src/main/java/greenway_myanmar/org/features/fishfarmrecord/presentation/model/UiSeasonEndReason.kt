package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason

data class UiSeasonEndReason(
    val id: String,
    val name: String
) {
    companion object {
        fun fromDomainModel(domainModel: SeasonEndReason) = UiSeasonEndReason(
            id = domainModel.id,
            name = domainModel.name
        )
    }
}

fun UiSeasonEndReason.asDomainModel() = SeasonEndReason(
    id = id,
    name = name
)