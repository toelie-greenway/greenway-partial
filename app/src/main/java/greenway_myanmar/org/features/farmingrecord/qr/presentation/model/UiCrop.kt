package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Crop

data class UiCrop(
    val id: String,
    val title: String,
) {
    companion object {
        fun fromDomain(domainEntity: Crop) = UiCrop(
            id = domainEntity.id,
            title = domainEntity.title
        )
    }
}