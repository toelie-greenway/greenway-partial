package greenway_myanmar.org.features.fishfarmrecord.data.source.network

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmListResponse
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeason
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFarmRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkSeasonRequest

/**
 * Interface representing network calls to the GreenWay backend
 */
interface FishFarmRecordNetworkDataSource {
    suspend fun postFarm(userId: String, request: NetworkFarmRequest): NetworkFarm
    suspend fun postSeason(
        userId: String,
        farmId: String,
        request: NetworkSeasonRequest
    ): NetworkSeason

    suspend fun postExpense(userId: String, request: NetworkExpenseRequest): NetworkExpense
    suspend fun getFarms(userId: String): NetworkFarmListResponse

    suspend fun getExpenseCategories(seasonId: String): List<NetworkExpenseCategory>
    suspend fun getFarmInputProducts(
        query: String,
        categoryId: String
    ): List<NetworkFarmInputProduct>

    suspend fun getFarmInputProductCategories(): List<NetworkFarmInputProductCategory>
    suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany

    suspend fun getFishes(): List<NetworkFish>
}