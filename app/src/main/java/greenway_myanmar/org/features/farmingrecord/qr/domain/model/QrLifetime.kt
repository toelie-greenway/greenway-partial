package greenway_myanmar.org.features.farmingrecord.qr.domain.model

data class QrLifetime(
    val month: Int
) {
    companion object {
        val Empty = QrLifetime(-1)
    }
}