package greenway_myanmar.org.features.farmingrecord.qr.data.api.responses

import com.google.gson.annotations.SerializedName

data class ApiDataResponse<T>(
    @SerializedName("data") val data: T
)
