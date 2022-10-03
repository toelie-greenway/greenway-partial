package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrInfo(
    val qrId: String,
    val farmLocationType: FarmLocationType,
    val optInShowPhone: Boolean,
    val optInShowFarmInput: Boolean,
    val optInShowYield: Boolean,
    val phone: String,
    val qrLifetime: QrLifetime
) {
    companion object {
        val Empty = QrInfo(
            qrId = "",
            farmLocationType = DEFAULT_FARM_LOCATION_TYPE,
            optInShowPhone = false,
            optInShowFarmInput = false,
            optInShowYield = false,
            phone = "",
            qrLifetime = QrLifetime.Empty
        )
    }
}