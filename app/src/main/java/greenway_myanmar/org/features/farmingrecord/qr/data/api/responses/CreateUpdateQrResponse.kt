package greenway_myanmar.org.features.farmingrecord.qr.data.api.responses

import com.google.gson.annotations.SerializedName

data class CreateUpdateQrResponse(
    @SerializedName("farm_id") val farmId: String,
    @SerializedName("season_id") val seasonId: String,
    @SerializedName("farm_location") val farmLocationType: String,
    @SerializedName("is_display_ph") val optInShowPhone: Boolean,
    @SerializedName("is_display_farm_input") val optInShowFarmInput: Boolean,
    @SerializedName("is_display_farm_output") val optInShowYield: Boolean,
    @SerializedName("qr_id") val qrId: String
)