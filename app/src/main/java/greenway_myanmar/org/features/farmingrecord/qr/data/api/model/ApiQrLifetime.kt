package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrLifetime

data class ApiQrLifetime(
    val month: Int
) {

    fun toDomain() = QrLifetime(
        month = month
    )

    companion object {
        fun from(month: Int) =
            ApiQrLifetime(
                month = month
            )
    }
}