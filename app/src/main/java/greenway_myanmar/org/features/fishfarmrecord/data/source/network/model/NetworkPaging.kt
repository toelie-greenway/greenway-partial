package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

data class NetworkPaging(
    val total: Int? = 0,
    val per_page: Int? = 0,
    val current_page: Int? = 0,
    val last_page: Int? = 0,
    val from: Int? = 0,
    val to: Int? = 0
)