package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

data class ApiQrQuantityListResponse(
    @SerializedName("data") val data: List<ApiQrQuantity>
)