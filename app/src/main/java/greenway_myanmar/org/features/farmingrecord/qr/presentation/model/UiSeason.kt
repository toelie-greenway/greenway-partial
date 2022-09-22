package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season
import greenway_myanmar.org.vo.SingleListItem

data class UiSeason(
    val id: String,
    val name: String,
    val crop: UiCrop
) : SingleListItem {
    override val itemId: Long
        get() = id.toLong()
    override val displayText: String
        get() = "$name (${crop.title})"

    companion object {
        fun fromDomain(domainEntity: Season): UiSeason {
            return UiSeason(
                id = domainEntity.id,
                name = domainEntity.seasonName,
                crop = UiCrop.fromDomain(domainEntity.crop)
            )
        }
    }
}