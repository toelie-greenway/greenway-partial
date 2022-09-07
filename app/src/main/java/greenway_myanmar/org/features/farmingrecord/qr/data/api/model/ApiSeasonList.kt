package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.api.Pagination

data class ApiSeasonList(
    @SerializedName("meta") val pagination: Pagination,
    @SerializedName("data") val data: List<ApiSeason>
)
