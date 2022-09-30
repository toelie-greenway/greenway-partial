package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.VillageTractTown
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.VillageWard

data class UiVillageTractTown(
    val id: String,
    val name: String,
) {
    companion object {
        fun fromDomain(domainEntity: VillageTractTown) = UiVillageTractTown(
            id = domainEntity.id,
            name = domainEntity.name
        )
    }
}