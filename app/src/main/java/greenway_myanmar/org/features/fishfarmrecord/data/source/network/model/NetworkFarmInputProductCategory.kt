package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFarmInputProductCategory(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val thumbnail: String? = null
)

fun NetworkFarmInputProductCategory.asDomainModel() = FarmInputProductCategory(
    id = id,
    name = name.orEmpty(),
    description = description.orEmpty(),
    thumbnail = thumbnail.orEmpty(),
)