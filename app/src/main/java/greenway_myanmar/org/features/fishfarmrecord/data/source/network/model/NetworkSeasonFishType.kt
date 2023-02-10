package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeasonFishType(
    val fish_detail: FishDetail? = null,
    val fish_id: String? = null,
    val id: String? = null,
    val specie: String? = null
) {
    @Serializable
    data class FishDetail(
        val en_name: String? = null,
        val id: String? = null,
        val image: String? = null,
        val name: String? = null,
        val scientific_name: String? = null
    )
}