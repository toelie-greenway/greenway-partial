package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Crop
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season
import java.time.Instant

data class ApiSeason(
    @SerializedName("id") val id: String? = null,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("season") val seasonName: String? = null,
    @SerializedName("start_planting_date") val startPlantingDate: String? = null,
    @SerializedName("crop_specie") val cropSpecie: String? = null,
    @SerializedName("crop") val crop: ApiCrop? = null
) {
    fun toDomain(latestHarvestedDate: Instant? = null): Season {
        return Season(
            id = id.orEmpty(),
            seasonName = seasonName.orEmpty(),
            crop = crop?.toDomain() ?: ApiCrop.Empty.toDomain(),
            latestHarvestedDate = latestHarvestedDate
        )
    }

    companion object {
        val Empty = ApiSeason()
    }
}