package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Township

data class UiTownship(
    val id: String,
    val name: String,
){
    companion object {
        fun fromDomain(domainEntity: Township) = UiTownship(
            id = domainEntity.id,
            name = domainEntity.name
        )
    }
}