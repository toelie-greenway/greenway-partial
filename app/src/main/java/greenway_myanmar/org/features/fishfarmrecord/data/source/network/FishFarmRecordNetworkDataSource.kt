package greenway_myanmar.org.features.fishfarmrecord.data.source.network

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory

/**
 * Interface representing network calls to the GreenWay backend
 */
interface FishFarmRecordNetworkDataSource {
    suspend fun getExpenseCategories(): List<NetworkExpenseCategory>
    suspend fun getFarmInputProducts(
        query: String,
        categoryId: String
    ): List<NetworkFarmInputProduct>

    suspend fun getFarmInputProductCategories(): List<NetworkFarmInputProductCategory>
    suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany
}