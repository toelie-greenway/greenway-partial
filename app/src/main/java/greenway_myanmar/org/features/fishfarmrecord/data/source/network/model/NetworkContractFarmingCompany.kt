package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import kotlinx.serialization.Serializable

@Serializable
data class NetworkContractFarmingCompany(
    val id: String,
    val name: String,
    val company_code: String,
)

fun NetworkContractFarmingCompany.asDomainModel() = ContractFarmingCompany(
    id = id,
    name = name,
    code = company_code
)