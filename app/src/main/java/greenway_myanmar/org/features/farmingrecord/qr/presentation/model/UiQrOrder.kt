package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder

data class UiQrOrder(
    val id: String,
    val farmInfo: String,
    val qrUrl: String
) {
    companion object {
        fun fromDomain(domainEntity: QrOrder): UiQrOrder {
            return UiQrOrder(
                id = domainEntity.id,
                farmInfo = domainEntity.farmInfo,
                qrUrl = domainEntity.qrUrl
            )
        }
    }
}