package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrDetail(
    val id: String,
    val qrInfo: QrInfo,
    val qrUrl: String,
    val qrIdNumber: String,
    val user: QrUser,
    val farm: Farm,
    val season: Season,
    val farmActivities: List<FarmActivity>
)