package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.*

data class UiQrDetail(
    val id: String,
    val qrInfo: UiQrInfo,
    val qrUrl: String,
    val qrIdNumber: String,
    val user: UiQrUser,
    val farm: UiFarm,
    val season: UiSeason,
    val farmActivities: List<UiFarmActivity>
) {
    companion object {
        fun fromDomain(domainEntity: QrDetail): UiQrDetail {
            return UiQrDetail(
                id = domainEntity.id,
                qrInfo = UiQrInfo.fromDomain(domainEntity.qrInfo),
                qrUrl = domainEntity.qrUrl,
                qrIdNumber = domainEntity.qrIdNumber,
                user = UiQrUser.fromDomain(domainEntity.user),
                farm = UiFarm.fromDomain(domainEntity.farm),
                season = UiSeason.fromDomain(domainEntity.season),
                farmActivities = domainEntity.farmActivities.map {
                    UiFarmActivity.fromDomain(it)
                }
            )
        }
    }
}