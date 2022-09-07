package greenway_myanmar.org.features.farmingrecord.qr.data.api.responses

import com.google.gson.annotations.SerializedName

data class CreateQrOrderResponse(
    @SerializedName("status") val status: String,
    @SerializedName("qr_id") val qrId: String,
    @SerializedName("qr_order_id") val qrOrderId: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val qrOrderStatusId: String //TODO: TBC
)
