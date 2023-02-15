package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

data class NetworkProductionRecord(
    val id: String? = null,
    val season_id: String? = null,
    val date: String? = null,
    val total: Double? = null,
    val productions: List<NetworkProductionPerFishType>? = null,
    val note: String? = null
)