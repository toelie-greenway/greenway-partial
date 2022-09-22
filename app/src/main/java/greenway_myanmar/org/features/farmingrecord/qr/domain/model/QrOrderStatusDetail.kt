package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.time.Instant

data class QrOrderStatusDetail(
    val id: String,
    val status: QrOrderStatus,
    val statusLabel: String,
    val note: String,
    val createdAt: Instant
)

