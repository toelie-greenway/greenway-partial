package greenway_myanmar.org.features.farmingrecord.qr.data.api.model


import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Crop

data class ApiCrop(
    @SerializedName("cover")
    val cover: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("crop_life_type")
    val cropLifeType: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("is_main_crop")
    val isMainCrop: Int? = null,
    @SerializedName("is_rwcp_crop")
    val isRwcpCrop: Int? = null,
    @SerializedName("order")
    val order: Int? = null,
    @SerializedName("parent_id")
    val parentId: Int? = null,
    @SerializedName("priority")
    val priority: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
) {
    fun toDomain() = Crop(
        id = id?.toString().orEmpty(),
        title = title.orEmpty()
    )

    companion object {
        val Empty = ApiCrop()
    }
}