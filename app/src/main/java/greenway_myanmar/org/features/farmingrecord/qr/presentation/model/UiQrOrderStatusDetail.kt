package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatus
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatusDetail
import java.time.Instant

data class UiQrOrderStatusDetail(
    val id: String,
    val status: QrOrderStatus,
    val description: String,
    val createdAt: Instant
) {
    companion object {
        fun fromDomain(domainModel: QrOrderStatusDetail) = UiQrOrderStatusDetail(
            id = domainModel.id,
            status = domainModel.status,
            description = domainModel.description,
            createdAt = domainModel.createdAt
        )
    }
}

