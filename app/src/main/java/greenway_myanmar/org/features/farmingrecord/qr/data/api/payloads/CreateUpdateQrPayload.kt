package greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads

import com.google.gson.annotations.SerializedName

data class CreateUpdateQrPayload(
    @SerializedName("farm_id") val farmId: String,
    @SerializedName("season_id") val seasonId: String,
    @SerializedName("farm_location") val farmLocationType: String,
    @SerializedName("is_display_ph") val optInShowPhone: Boolean,
    @SerializedName("is_display_farm_input") val optInShowFarmInput: Boolean,
    @SerializedName("is_display_farm_output") val optInShowYield: Boolean,
    @SerializedName("phone") val phone: String,
    @SerializedName("qr_type") val qrType: String = "individual",
    @SerializedName("qr_lifetime") val qrLifetime: Int
)