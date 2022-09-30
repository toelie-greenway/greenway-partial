package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.VillageWard

data class UiVillageWard(
    val id: String,
    val name: String
) {
    companion object {
        fun fromDomain(domainEntity: VillageWard) = UiVillageWard(
            id = domainEntity.id,
            name = domainEntity.name
        )
    }
}