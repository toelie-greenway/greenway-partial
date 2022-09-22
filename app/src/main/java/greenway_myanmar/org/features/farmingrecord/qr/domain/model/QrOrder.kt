package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrOrder(
    val id: String,
    val farm: Farm,
    val season: Season,
    val qrUrl: String,
    val qrIdNumber: String,
    val quantity: Int,
    val latestStatus: QrOrderStatusDetail,
    val statuses: List<QrOrderStatusDetail>
)