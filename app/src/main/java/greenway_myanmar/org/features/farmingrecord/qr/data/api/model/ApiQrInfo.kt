package greenway_myanmar.org.features.farmingrecord.qr.data.api.model


import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrInfo
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrLifetime

data class ApiQrInfo(
    @SerializedName("qr_id")
    val qrId: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("farm_id")
    val farmId: Int? = null,
    @SerializedName("farm_location")
    val farmLocation: String? = null,
    @SerializedName("is_display_farm_input")
    val isDisplayFarmInput: Boolean? = null,
    @SerializedName("is_display_farm_output")
    val isDisplayFarmOutput: Boolean? = null,
    @SerializedName("is_display_ph")
    val isDisplayPh: Boolean? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("qr_type")
    val qrType: String? = null,
    @SerializedName("season_id")
    val seasonId: Int? = null,
    @SerializedName("qr_lifetime")
    val qrLifetime: Int? = null
) {
    fun toDomain() = QrInfo(
        qrId = qrId.orEmpty(),
        farmLocationType = FarmLocationType.fromString(farmLocation.orEmpty()),
        optInShowPhone = isDisplayPh == true,
        optInShowFarmInput = isDisplayFarmInput == true,
        optInShowYield = isDisplayFarmOutput == true,
        phone = phone.orEmpty(),
        qrLifetime = QrLifetime(15) //
    )

    companion object {
        val Empty = ApiQrInfo()
    }
}