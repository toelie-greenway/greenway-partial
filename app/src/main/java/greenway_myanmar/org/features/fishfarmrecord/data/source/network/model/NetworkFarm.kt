package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFarm(
    val id: String,
    val name: String,
    val ownership: String,
    val photos: List<String>?,
    val plot_id: String?,
    val lat: Double?,
    val lon: Double?,
    val area: NetworkFarmAreaRequest
)

@Serializable
data class NetworkFarmArea(
    val id: String,
    val acre: Double,
    val measurement_type: String?,
    val depth: Double?,
    val measurement: List<NetworkFarmAreaLatLng>
)

@Serializable
data class NetworkFarmAreaLatLng(
    val lat: Double,
    val lng: Double
)