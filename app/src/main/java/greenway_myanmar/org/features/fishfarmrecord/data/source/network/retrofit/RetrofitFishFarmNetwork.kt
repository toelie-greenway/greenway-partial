package greenway_myanmar.org.features.fishfarmrecord.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.ApiDataWrapper
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmListResponse
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeason
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkFarmRequest
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkSeasonRequest
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for GreenWay Fish Farm Record Feature Network API
 */
private interface RetrofitFishFarmRecordNetworkApi {

    @POST("ffr/farms")
    suspend fun postFarm(
        @Query("user_id") userId: String,
        @Body body: NetworkFarmRequest
    ): ApiResponse<ApiDataWrapper<NetworkFarm>>

    @POST("ffr/farms/{farm_id}/seasons")
    suspend fun postSeason(
        @Path("farm_id") farmId: String,
        @Query("user_id") userId: String,
        @Body body: NetworkSeasonRequest
    ): ApiResponse<ApiDataWrapper<NetworkSeason>>

    @POST("ffr/expenses")
    suspend fun postExpense(
        @Query("user_id") userId: String,
        @Body body: NetworkExpenseRequest
    ): ApiResponse<ApiDataWrapper<NetworkExpense>>

    @GET(value = "ffr/farms")
    suspend fun getFarms(
        @Query("user_id") userId: String,
        @Query("is_harvest") isHarvest: Int = 0
    ): ApiResponse<NetworkFarmListResponse>

    @GET(value = "ffr/farms/{farm_id}")
    suspend fun getFarm(
        @Path("farm_id") farmId: String,
        @Query("user_id") userId: String
    ): ApiResponse<ApiDataWrapper<NetworkFarm>>

    @GET(value = "ffr/expense-categories")
    suspend fun getExpenseCategories(
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkExpenseCategory>>>

    @GET(value = "ffr/fish-types")
    suspend fun getFishes(): ApiResponse<List<NetworkFish>>

    // TODO: use new ffr/inputs api
    @GET(value = "asymt/season-end-reasons")
    suspend fun getSeasonEndReasons(): ApiResponse<ApiDataWrapper<List<NetworkSeasonEndReason>>>

    // TODO: use existing asymt/inputs api?
    @GET(value = "ffr/inputs")
    suspend fun getFarmInputProducts(
        @Query("page") page: Int = 1,
        @Query("category_id") categoryId: String,
        @Query("q") query: String
    ): ApiResponse<ApiDataWrapper<List<NetworkFarmInputProduct>>>

    // TODO: use existing marketplace/categories api?
    @GET(value = "ffr/product-categories")
    suspend fun getProductCategories(): ApiResponse<List<NetworkFarmInputProductCategory>>

    // TODO: use existing asymt/check-company api?
    @GET(value = "asymt/check-company")
    suspend fun getCompanyByCode(
        @Query("company_code") code: String
    ): ApiResponse<ApiDataWrapper<NetworkContractFarmingCompany>>

}

/**
 * [Retrofit] backed [FishFarmRecordNetworkDataSource]
 */
@Singleton
class RetrofitFishFarmNetworkDataSource @Inject constructor(
    retrofit: Retrofit
) : FishFarmRecordNetworkDataSource {

    private val networkApi: RetrofitFishFarmRecordNetworkApi =
        retrofit.create(RetrofitFishFarmRecordNetworkApi::class.java)

    override suspend fun postFarm(userId: String, request: NetworkFarmRequest) =
        networkApi.postFarm(userId, request).getDataOrThrow().data

    override suspend fun postSeason(
        userId: String,
        farmId: String,
        request: NetworkSeasonRequest
    ) = networkApi.postSeason(
        farmId = farmId,
        userId = userId,
        body = request
    ).getDataOrThrow().data

    override suspend fun postExpense(
        userId: String,
        request: NetworkExpenseRequest
    ): NetworkExpense =
        networkApi.postExpense(userId, request).getDataOrThrow().data

    override suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany =
        networkApi.getCompanyByCode(code).getDataOrThrow().data

    override suspend fun getFarm(farmId: String, userId: String): NetworkFarm =
        networkApi.getFarm(farmId, userId).getDataOrThrow().data

    override suspend fun getFarms(userId: String): NetworkFarmListResponse =
        networkApi.getFarms(userId).getDataOrThrow()

    override suspend fun getFarmInputProducts(
        query: String,
        categoryId: String
    ): List<NetworkFarmInputProduct> =
        networkApi.getFarmInputProducts(
            query = query,
            categoryId = categoryId
        ).getDataOrThrow().data

    override suspend fun getFarmInputProductCategories(): List<NetworkFarmInputProductCategory> =
        networkApi.getProductCategories().getDataOrThrow()


    override suspend fun getExpenseCategories(seasonId: String): List<NetworkExpenseCategory> =
        networkApi.getExpenseCategories(seasonId).getDataOrThrow().data

    override suspend fun getFishes(): List<NetworkFish> =
        networkApi.getFishes().getDataOrThrow()

    override suspend fun getSeasonEndReasons(): List<NetworkSeasonEndReason> =
        networkApi.getSeasonEndReasons().getDataOrThrow().data
}