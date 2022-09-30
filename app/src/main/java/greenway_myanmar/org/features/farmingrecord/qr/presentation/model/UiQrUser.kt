package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.*

data class UiQrUser(
    val id: String,
    val name: String,
    val phone: String,
    val avatar: String,
    val crops: List<Crop>,
    val stateRegion: UiStateRegion? = null,
    val township: UiTownship? = null,
    val villageTractTown: UiVillageTractTown? = null,
    val villageWard: UiVillageWard? = null
) {

    companion object {
        private fun mapStateRegion(domainEntity: StateRegion?): UiStateRegion? {
            if (domainEntity == null) return null

            return UiStateRegion.fromDomain(domainEntity)
        }

        private fun mapTownship(domainEntity: Township?): UiTownship? {
            if (domainEntity == null) return null

            return UiTownship.fromDomain(domainEntity)
        }

        private fun mapVillageTractTown(domainEntity: VillageTractTown?): UiVillageTractTown? {
            if (domainEntity == null) return null

            return UiVillageTractTown.fromDomain(domainEntity)
        }

        private fun mapVillageWard(domainEntity: VillageWard?): UiVillageWard? {
            if (domainEntity == null) return null

            return UiVillageWard.fromDomain(domainEntity)
        }

        fun fromDomain(domainEntity: QrUser) = UiQrUser(
            id = domainEntity.id,
            name = domainEntity.name,
            avatar = domainEntity.avatar,
            phone = domainEntity.phone,
            crops = domainEntity.crops,
            stateRegion = mapStateRegion(domainEntity.stateRegion),
            township = mapTownship(domainEntity.township),
            villageTractTown = mapVillageTractTown(domainEntity.villageTractTown),
            villageWard = mapVillageWard(domainEntity.villageWard)
        )
    }
}