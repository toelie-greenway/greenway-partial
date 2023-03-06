package greenway_myanmar.org.features.template.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTemplate(
    val id: String? = null,
    val title: String? = null,
    val image: String? = null
)