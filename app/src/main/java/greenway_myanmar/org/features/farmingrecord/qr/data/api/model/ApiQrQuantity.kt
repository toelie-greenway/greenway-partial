package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrQuantity

data class ApiQrQuantity(
    val quantity: Int,
    @SerializedName("expected_price") val price: String
) {
    fun toDomain() = QrQuantity(
        quantity = quantity,
        price = price
    )
}