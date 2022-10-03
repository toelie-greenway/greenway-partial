package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrInfo

data class UiQrInfo(
    val farmLocationType: FarmLocationType,
    val optInShowPhone: Boolean,
    val optInShowFarmInput: Boolean,
    val optInShowYield: Boolean,
    val phone: String,
    val qrLifetime: UiQrLifetime
) {
    companion object {
        fun fromDomain(domainEntity: QrInfo) = UiQrInfo(
            farmLocationType = domainEntity.farmLocationType,
            optInShowPhone = domainEntity.optInShowPhone,
            optInShowFarmInput = domainEntity.optInShowFarmInput,
            optInShowYield = domainEntity.optInShowYield,
            phone = domainEntity.phone,
            qrLifetime = UiQrLifetime.fromDomain(domainEntity.qrLifetime)
        )
    }
}