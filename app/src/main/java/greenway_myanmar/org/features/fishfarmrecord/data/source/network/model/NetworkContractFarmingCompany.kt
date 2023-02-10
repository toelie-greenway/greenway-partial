package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import kotlinx.serialization.Serializable

@Serializable
data class NetworkContractFarmingCompany(
    val id: String? = null,
    val name: String? = null,
    val company_code: String? = null,
)

fun NetworkContractFarmingCompany.asDomainModel() = ContractFarmingCompany(
    id = id ?: throw IllegalStateException("id must not be null"),
    name = name.orEmpty(),
    code = company_code.orEmpty()
)