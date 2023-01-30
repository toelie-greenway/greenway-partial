package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ContractFarmingCompanyRepository
import javax.inject.Inject

class GetContractFarmingCompanyByCode @Inject constructor(
    private val repository: ContractFarmingCompanyRepository
) {
    suspend operator fun invoke(code: String): ContractFarmingCompany {
        return repository.getCompanyByCode(code)
    }
}