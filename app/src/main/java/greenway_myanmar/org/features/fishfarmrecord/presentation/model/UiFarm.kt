package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season

data class UiFarm(
    val id: String,
    val name: String,
    val images: List<String>,
    val ongoingSeason: Season? = null,
    val area: UiArea? = null,

) {
    companion object {
        fun fromDomain(domainEntity: Farm) = UiFarm(
            id = domainEntity.id,
            name = domainEntity.name,
            images = domainEntity.images.orEmpty(),
            ongoingSeason = domainEntity.openingSeason,
            area = mapArea(domainEntity.measurement.area)
        )

        private fun mapArea(area: Area?): UiArea? {
            if (area == null) return null

            return UiArea.fromDomain(area)
        }
    }
}
