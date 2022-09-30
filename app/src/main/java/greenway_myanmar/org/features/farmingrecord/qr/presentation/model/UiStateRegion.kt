package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.StateRegion

data class UiStateRegion(
    val id: String,
    val name: String,
){
    companion object {
        fun fromDomain(domainEntity: StateRegion) = UiStateRegion(
            id = domainEntity.id,
            name = domainEntity.name
        )
    }
}