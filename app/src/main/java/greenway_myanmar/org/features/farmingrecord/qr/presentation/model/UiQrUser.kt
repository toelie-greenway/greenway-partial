package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrUser

data class UiQrUser(
    val id: String,
    val name: String,
    val avatar: String,
    val userInfo: UiQrUserInfo
) {
    data class UiQrUserInfo(
        val career: String
    ) {
        companion object {
            val Empty = UiQrUserInfo(
                career = ""
            )

            fun fromDomain(domainEntity: QrUser.QrUserInfo) = UiQrUserInfo(
                career = domainEntity.career
            )
        }
    }

    companion object {
        fun fromDomain(domainEntity: QrUser) = UiQrUser(
            id = domainEntity.id,
            name = domainEntity.name,
            avatar = domainEntity.avatar,
            userInfo = UiQrUserInfo.fromDomain(domainEntity.userInfo)
        )
    }
}