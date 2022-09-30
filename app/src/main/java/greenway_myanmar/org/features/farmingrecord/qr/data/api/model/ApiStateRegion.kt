package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.StateRegion

data class ApiStateRegion(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
) {
    fun toDomain() = StateRegion(
        id = id.orEmpty(),
        name = name.orEmpty()
    )
}