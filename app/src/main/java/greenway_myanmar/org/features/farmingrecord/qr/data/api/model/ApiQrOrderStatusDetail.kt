package greenway_myanmar.org.features.farmingrecord.qr.data.api.model


import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatus
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrderStatusDetail
import java.time.Instant

data class ApiQrOrderStatusDetail(
    @SerializedName("created_at")
    val createdAt: Instant? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("qr_id")
    val qrId: String? = null,
    @SerializedName("qr_order_id")
    val qrOrderId: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
) {
    fun toDomain(): QrOrderStatusDetail {
        return QrOrderStatusDetail(
            id = id.orEmpty(),
            status = QrOrderStatus.fromString(status.orEmpty()),
            description = description.orEmpty(),
            createdAt = createdAt ?: Instant.now()
        )
    }
}