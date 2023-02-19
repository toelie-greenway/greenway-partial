package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.util.Image
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val url: String? = null
)


fun NetworkImage.asDomainModel(): Image = Image.from(url.orEmpty())