package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmAreaLatLng
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFarmRequest(
    val name: String,
    val ownership: String,
    val photos: List<String>?,
    val plot_id: String?,
    val area: NetworkFarmAreaRequest
)

@Serializable
data class NetworkFarmAreaRequest(
    val acre: Double,
    val measurement_type: String?,
    val measured_acre: Double?,
    val depth: Double?,
    val lat: Double?,
    val lon: Double?,
    val measurement: List<NetworkFarmAreaLatLng>?
)
