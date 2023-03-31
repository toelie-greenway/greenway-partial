package greenway_myanmar.org.features.template.data.source.network.model

import greenway_myanmar.org.vo.Image
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val url: String? = null
)


fun NetworkImage.asDomainModel(): Image = Image.from(url.orEmpty())