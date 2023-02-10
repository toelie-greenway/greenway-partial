package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

data class NetworkFarmListResponse(
    val meta: NetworkPaging? = null,
    val data: List<NetworkFarm>? = null
)