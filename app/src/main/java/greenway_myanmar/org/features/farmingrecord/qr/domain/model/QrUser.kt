package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrUser(
    val id: String,
    val name: String,
    val avatar: String,
    val userInfo: QrUserInfo
) {
    data class QrUserInfo(
        val career: String
    ) {
        companion object {
            val Empty = QrUserInfo(
                career = ""
            )
        }
    }
}