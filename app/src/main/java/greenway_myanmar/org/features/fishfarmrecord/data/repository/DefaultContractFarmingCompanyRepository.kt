package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ContractFarmingCompanyRepository
import javax.inject.Inject

class DefaultContractFarmingCompanyRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource
) : ContractFarmingCompanyRepository {
    override suspend fun getCompanyByCode(code: String): ContractFarmingCompany {
        return network.getCompanyByCode(code).asDomainModel()
    }
}