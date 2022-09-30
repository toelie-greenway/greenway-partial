package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FARM_DEFAULT_AREA_UNIT
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import java.util.*

data class ApiFarm(
    @SerializedName("id") val id: String? = null,
    @SerializedName("area") val area: Double? = null,
    @SerializedName("area_unit") val areaUnit: String? = "acre",
    @SerializedName("location") val location: String? = "",
    @SerializedName("lat") val latitude: Double? = null,
    @SerializedName("lon") val longitude: Double? = null,
    @SerializedName("name") val name: String? = "",
    @SerializedName("ownership") val ownership: String? = "",
    @SerializedName("photos") val photos: List<String>? = null,
    @SerializedName("plot_number") val plotNumber: String? = "",
    @SerializedName("plot_id") val plotId: String? = "",
    @SerializedName("created_at") val createdDate: Date? = null,
) {
    fun toDomain(): Farm {
        return Farm(
            id = id.orEmpty(),
            name = name.orEmpty(),
            area = area ?: 0.0,
            areaUnit = areaUnit ?: FARM_DEFAULT_AREA_UNIT,
            images = photos.orEmpty(),
            location = location.orEmpty(),
            latitude = latitude,
            longitude = longitude
        )
    }

    companion object {
        val Empty = ApiFarm()
    }
}