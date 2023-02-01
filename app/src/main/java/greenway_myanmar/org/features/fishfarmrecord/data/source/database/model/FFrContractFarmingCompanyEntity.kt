package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany

@Entity(
    tableName = "ffr_contract_farming_companies"
)
data class FFrContractFarmingCompanyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val code: String = ""
) {
    companion object {
        fun fromDomainModel(domainModel: ContractFarmingCompany) = FFrContractFarmingCompanyEntity(
            id = domainModel.id,
            name = domainModel.name,
            code = domainModel.code
        )
    }
}

fun FFrContractFarmingCompanyEntity.asDomainModel() = ContractFarmingCompany(
    id = id,
    name = name,
    code = code
)