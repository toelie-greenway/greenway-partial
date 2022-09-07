package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season

data class ApiSeason(
    @SerializedName("id") val id: String,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("season") val seasonName: String? = null,
    @SerializedName("start_planting_date") val startPlantingDate: String? = null,
    @SerializedName("crop_specie") val cropSpecie: String? = null,
) {
    fun toDomain(): Season {
        return Season(
            id = id,
            seasonName = seasonName.orEmpty()
        )
    }
}