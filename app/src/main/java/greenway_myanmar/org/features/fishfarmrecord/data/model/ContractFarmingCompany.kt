package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FFrContractFarmingCompanyEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany

fun NetworkContractFarmingCompany.asEntity() = FFrContractFarmingCompanyEntity(
    id = id.orEmpty(),
    name = name.orEmpty(),
    code = company_code.orEmpty()
)