package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLoan(
    val id: String? = null,
    val amount: Double? = null,
    val created_at: String? = null,
    val duration: Int? = null,
    val landing_organization: String? = null,
    val remark: String? = null
)