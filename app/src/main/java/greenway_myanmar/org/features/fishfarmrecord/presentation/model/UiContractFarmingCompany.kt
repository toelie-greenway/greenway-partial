package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany

data class UiContractFarmingCompany(
    val id: String,
    val name: String
) {
    companion object {
        fun fromDomain(domainModel: ContractFarmingCompany) = UiContractFarmingCompany(
            id = domainModel.id,
            name = domainModel.name
        )
    }
}
