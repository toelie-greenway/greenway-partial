package greenway_myanmar.org.features.farmingrecord.qr.domain.model

import java.time.Instant

data class QrOrderStatus(
    val id: Int,
    val status: String,
    val note: String?,
    val date: Instant
)
