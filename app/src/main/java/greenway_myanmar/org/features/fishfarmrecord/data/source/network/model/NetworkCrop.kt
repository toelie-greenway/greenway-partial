package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Crop
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCrop(
    val id: String? = null,
    val title: String? = null,
    val image: String? = null
)

fun NetworkCrop.asDomainModel() = Crop(
    id = id.orEmpty(),
    name = title.orEmpty(),
    iconImageUrl = image.orEmpty()
)