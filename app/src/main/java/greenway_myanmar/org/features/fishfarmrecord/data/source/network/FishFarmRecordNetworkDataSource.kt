package greenway_myanmar.org.features.fishfarmrecord.data.source.network

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkCategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkCropIncome
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmListResponse
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFcrRecord
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkImage
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionRecord
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeason
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonListResponse
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonSummary
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkCropIncomeRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFarmRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkSeasonRequest
import okhttp3.RequestBody

/**
 * Interface representing network calls to the GreenWay backend
 */
interface FishFarmRecordNetworkDataSource {

    suspend fun postImage(
        params: Map<String, RequestBody?>
    ): NetworkImage

    suspend fun postFarm(userId: String, request: NetworkFarmRequest): NetworkFarm
    suspend fun postSeason(
        userId: String,
        farmId: String,
        request: NetworkSeasonRequest
    ): NetworkSeason

    suspend fun postExpense(userId: String, request: NetworkExpenseRequest): NetworkExpense

    suspend fun postFcrRecord(
        userId: String,
        request: NetworkFcrRecordRequest
    ): NetworkFcrRecord

    suspend fun postProductionRecord(
        userId: String,
        request: NetworkProductionRecordRequest
    ): NetworkProductionRecord

    suspend fun postCropIncome(
        userId: String,
        request: NetworkCropIncomeRequest
    ): NetworkCropIncome

    suspend fun getCategoryExpense(
        userId: String,
        categoryId: String,
        seasonId: String
    ): NetworkCategoryExpense

    suspend fun getCategoryExpenses(userId: String, seasonId: String): List<NetworkCategoryExpense>
    suspend fun getClosedSeasons(
        userId: String,
        farmId: String,
        page: Int
    ): NetworkSeasonListResponse

    suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany
    suspend fun getCropIncomes(
        userId: String,
        seasonId: String
    ): List<NetworkCropIncome>

    suspend fun getExpenseCategories(userId: String): List<NetworkExpenseCategory>
    suspend fun getFarm(farmId: String, userId: String): NetworkFarm
    suspend fun getFarmInputProducts(
        query: String,
        categoryId: String
    ): List<NetworkFarmInputProduct>

    suspend fun getFarmInputProductCategories(): List<NetworkFarmInputProductCategory>
    suspend fun getFarms(userId: String): NetworkFarmListResponse
    suspend fun getFcrRecords(
        userId: String,
        seasonId: String
    ): List<NetworkFcrRecord>

    suspend fun getFishes(): List<NetworkFish>
    suspend fun getProductionRecords(
        userId: String,
        seasonId: String
    ): List<NetworkProductionRecord>

    suspend fun getSeasonEndReasons(): List<NetworkSeasonEndReason>

    suspend fun getSeasonSummary(
        farmId: String,
        seasonId: String,
        userId: String
    ): NetworkSeasonSummary

    suspend fun patchSeason(
        farmId: String,
        seasonId: String,
        userId: String,
        fields: Map<String, Any>
    ): NetworkSeason
}