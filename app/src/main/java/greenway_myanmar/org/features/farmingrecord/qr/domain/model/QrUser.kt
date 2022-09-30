package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrUser(
    val id: String,
    val name: String,
    val phone: String,
    val avatar: String,
    val crops: List<Crop>,
    val stateRegion: StateRegion? = null,
    val township: Township? = null,
    val villageTractTown: VillageTractTown? = null,
    val villageWard: VillageWard? = null
) {
    companion object {
        val Empty = QrUser(
            id = "",
            name = "",
            avatar = "",
            phone = "",
            crops = emptyList()
        )
    }
}