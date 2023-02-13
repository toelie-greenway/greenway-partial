package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategoryType
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFarmInputProductCategory(
    val id: String,
    val title: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val type: String? = null,
    val is_fingerling: Boolean? = null
)

fun NetworkFarmInputProductCategory.asDomainModel() = FarmInputProductCategory(
    id = id,
    name = title.orEmpty(),
    thumbnail = thumbnail.orEmpty(),
    type = FarmInputProductCategoryType.fromString(type.orEmpty()),
    isFingerling = is_fingerling ?: false
)