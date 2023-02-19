package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFish(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null
) {
    companion object {
        fun fromDomainModel(domainModel: Fish) = NetworkFish(
            id = domainModel.id,
            name = domainModel.name,
            image = domainModel.iconImageUrl
        )
    }
}

fun NetworkFish.asDomainModel() = Fish(
    id = id.orEmpty(),
    name = name.orEmpty(),
    iconImageUrl = image.orEmpty(),
    species = ""
)