package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.*

data class UiQrDetail(
    val id: String,
    val qrUrl: String,
    val user: UiQrUser,
    val farm: UiFarm,
    val season: UiSeason,
    val farmActivities: List<UiQrFarmActivity>
) {
    companion object {
        fun fromDomain(domainEntity: QrDetail): UiQrDetail {
            return UiQrDetail(
                id = domainEntity.id,
                qrUrl = domainEntity.qrUrl,
                user = UiQrUser.fromDomain(domainEntity.user),
                farm = UiFarm.fromDomain(domainEntity.farm),
                season = UiSeason.fromDomain(domainEntity.season),
                farmActivities = domainEntity.farmActivities.map {
                    UiQrFarmActivity.fromDomain(it)
                }
            )
        }
    }
}