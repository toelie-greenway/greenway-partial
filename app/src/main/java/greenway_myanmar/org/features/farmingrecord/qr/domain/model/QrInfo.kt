package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrInfo(
    val farmLocationType: FarmLocationType,
    val optInShowPhone: Boolean,
    val optInShowFarmInput: Boolean,
    val optInShowYield: Boolean,
    val phone: String,
) {
    companion object {
        val Empty = QrInfo(
            farmLocationType = DEFAULT_FARM_LOCATION_TYPE,
            optInShowPhone = false,
            optInShowFarmInput = false,
            optInShowYield = false,
            phone = ""
        )
    }
}