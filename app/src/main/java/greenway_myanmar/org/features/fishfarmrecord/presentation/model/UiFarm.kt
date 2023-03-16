package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm

data class UiFarm(
    val id: String,
    val name: String,
    val images: List<String>,
    val ongoingSeason: UiSeason? = null,
    val area: UiArea,

    ) {
    companion object {
        fun fromDomain(domainEntity: Farm) = UiFarm(
            id = domainEntity.id,
            name = domainEntity.name,
            images = domainEntity.images.orEmpty().map { it.url },
            ongoingSeason = if (domainEntity.openingSeason != null) UiSeason.fromDomain(domainEntity.openingSeason) else null,
            area = UiArea.fromDomain(domainEntity.measurement.area)
        )
    }
}
