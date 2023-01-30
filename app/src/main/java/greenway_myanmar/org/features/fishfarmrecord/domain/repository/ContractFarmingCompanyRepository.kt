package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany

interface ContractFarmingCompanyRepository {
    suspend fun getCompanyByCode(code: String): ContractFarmingCompany
}