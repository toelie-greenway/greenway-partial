package greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads

import com.google.gson.annotations.SerializedName

data class CreateQrOrderPayload(
    @SerializedName("qr_id") val qrId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("qr_url") val qrUrl: String,
    @SerializedName("qr_id_number") val qrIdNumber: String
)