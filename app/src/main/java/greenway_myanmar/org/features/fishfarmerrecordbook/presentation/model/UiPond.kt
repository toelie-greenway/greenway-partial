package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.model

import com.greenwaymyanmar.core.domain.model.Area
import com.greenwaymyanmar.core.presentation.model.UiArea
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Pond
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Season

data class UiPond(
    val id: String,
    val name: String,
    val images: List<String>,
    val ongoingSeason: Season? = null,
    val area: UiArea? = null
) {
    companion object {
        fun fromDomain(domainEntity: Pond) = UiPond(
            id = domainEntity.id,
            name = domainEntity.name,
            images = domainEntity.images,
            ongoingSeason = domainEntity.ongoingSeason,
            area = mapArea(domainEntity.area)
        )

        private fun mapArea(area: Area?): UiArea? {
            if (area == null) return null

            return UiArea.fromDomain(area)
        }
    }
}
