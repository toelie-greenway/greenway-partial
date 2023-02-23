package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeason(
    val id: String,
    val area: NetworkFarmArea,
    val company: NetworkContractFarmingCompany? = null,
    val created_at: String? = null,
    val end_date: String? = null,
    val fish_types: List<NetworkSeasonFishType>? = null,
    val is_end: Boolean? = null,
    val is_harvest: Boolean? = null,
    val loan: NetworkLoan? = null,
    val season: String? = null,
    val total_cost: Double? = null,
    val total_income: Double? = null,
    val total_profit: Double? = null,
    val start_date: String? = null
)