package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatus
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatusDetail
import java.time.Instant

data class UiQrOrderStatusDetail(
    val id: String,
    val status: QrOrderStatus,
    val statusLabel: String,
    val createdAt: Instant
) {
    companion object {
        fun fromDomain(domainModel: QrOrderStatusDetail) = UiQrOrderStatusDetail(
            id = domainModel.id,
            status = domainModel.status,
            statusLabel = domainModel.statusLabel,
            createdAt = domainModel.createdAt
        )
    }
}

