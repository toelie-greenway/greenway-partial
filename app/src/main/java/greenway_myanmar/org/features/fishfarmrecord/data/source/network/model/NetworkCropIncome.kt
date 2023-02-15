package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCropIncome(
    val id: String,
    val date: String,
    val income: Double,
    val crop: NetworkCrop
)