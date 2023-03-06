package greenway_myanmar.org.features.fishfarmrecord.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.ApiDataWrapper
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
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for GreenWay Fish Farm Record Feature Network API
 */
private interface RetrofitFishFarmRecordNetworkApi {

    @Multipart
    @POST("upload")
    suspend fun postImage(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody?>
    ): ApiResponse<ApiDataWrapper<NetworkImage>>

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

    @POST("ffr/fcr-records")
    suspend fun postFcrRecord(
        @Query("user_id") userId: String,
        @Body body: NetworkFcrRecordRequest
    ): ApiResponse<ApiDataWrapper<NetworkFcrRecord>>

    @POST("ffr/productions")
    suspend fun postProductionRecord(
        @Query("user_id") userId: String,
        @Body body: NetworkProductionRecordRequest
    ): ApiResponse<ApiDataWrapper<NetworkProductionRecord>>

    @POST("ffr/crop-incomes")
    suspend fun postCropIncome(
        @Query("user_id") userId: String,
        @Body body: NetworkCropIncomeRequest
    ): ApiResponse<ApiDataWrapper<NetworkCropIncome>>

    @GET("ffr/farms/{farm_id}/seasons?is_end=1")
    suspend fun getClosedSeasons(
        @Path("farm_id") farmId: String,
        @Query("page") page: Int = 1,
        @Query("user_id") userId: String
    ): ApiResponse<NetworkSeasonListResponse>

    @GET("ffr/farms/{farm_id}/seasons/{season_id}")
    suspend fun getSeasonSummary(
        @Path("farm_id") farmId: String,
        @Path("season_id") seasonId: String,
        @Query("user_id") userId: String,
        @Query("summary") summary: Int = 1,
    ): ApiResponse<ApiDataWrapper<NetworkSeasonSummary>>

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
        @Query("user_id") userId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkExpenseCategory>>>

    @GET(value = "ffr/expense-categories/{category_id}")
    suspend fun getExpenseSubCategories(
        @Path("category_id") categoryId: String,
        @Query("user_id") userId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkExpenseCategory>>>

    @GET(value = "ffr/expense-categories/{category_id}/expenses")
    suspend fun getCategoryExpense(
        @Path("category_id") categoryId: String,
        @Query("user_id") userId: String,
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<NetworkCategoryExpense>>

    @GET(value = "ffr/expense-categories")
    suspend fun getCategoryExpenses(
        @Query("user_id") userId: String,
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkCategoryExpense>>>

    @GET(value = "ffr/crop-incomes")
    suspend fun getCropIncomes(
        @Query("user_id") userId: String,
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkCropIncome>>>

    @GET(value = "ffr/fcr-records")
    suspend fun getFcrRecords(
        @Query("user_id") userId: String,
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkFcrRecord>>>

    @GET(value = "ffr/fish-types")
    suspend fun getFishes(): ApiResponse<List<NetworkFish>>

    @GET(value = "ffr/productions")
    suspend fun getProductionRecords(
        @Query("user_id") userId: String,
        @Query("season_id") seasonId: String
    ): ApiResponse<ApiDataWrapper<List<NetworkProductionRecord>>>

    @GET(value = "ffr/season-end-reasons")
    suspend fun getSeasonEndReasons(): ApiResponse<ApiDataWrapper<List<NetworkSeasonEndReason>>>

    @GET(value = "ffr/inputs")
    suspend fun getFarmInputProducts(
        @Query("page") page: Int = 1,
        @Query("category_id") categoryId: String,
        @Query("q") query: String
    ): ApiResponse<ApiDataWrapper<List<NetworkFarmInputProduct>>>

    @GET(value = "ffr/input-categories")
    suspend fun getProductCategories(): ApiResponse<ApiDataWrapper<List<NetworkFarmInputProductCategory>>>

    @GET(value = "check-company")
    suspend fun getCompanyByCode(
        @Query("company_code") code: String
    ): ApiResponse<ApiDataWrapper<NetworkContractFarmingCompany>>

    @PATCH(value = "ffr/farms/{farm_id}/seasons/{season_id}")
    suspend fun patchSeason(
        @Path("farm_id") farmId: String,
        @Path("season_id") seasonId: String,
        @Query("user_id") userId: String,
        @Body fields: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<ApiDataWrapper<NetworkSeason>>
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

    override suspend fun postImage(params: Map<String, RequestBody?>): NetworkImage =
        networkApi.postImage(params).getDataOrThrow().data

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

    override suspend fun postFcrRecord(
        userId: String,
        request: NetworkFcrRecordRequest
    ): NetworkFcrRecord =
        networkApi.postFcrRecord(userId, request).getDataOrThrow().data

    override suspend fun postProductionRecord(
        userId: String,
        request: NetworkProductionRecordRequest
    ): NetworkProductionRecord =
        networkApi.postProductionRecord(
            userId = userId,
            body = request
        ).getDataOrThrow().data

    override suspend fun postCropIncome(
        userId: String,
        request: NetworkCropIncomeRequest
    ): NetworkCropIncome =
        networkApi.postCropIncome(
            userId = userId,
            body = request
        ).getDataOrThrow().data

    override suspend fun getCategoryExpense(
        userId: String,
        categoryId: String,
        seasonId: String
    ): NetworkCategoryExpense =
        networkApi.getCategoryExpense(
            categoryId = categoryId,
            seasonId = seasonId,
            userId = userId
        ).getDataOrThrow().data

    override suspend fun getCategoryExpenses(
        userId: String,
        seasonId: String
    ): List<NetworkCategoryExpense> =
        networkApi.getCategoryExpenses(userId, seasonId).getDataOrThrow().data

    override suspend fun getClosedSeasons(
        userId: String,
        farmId: String,
        page: Int
    ): NetworkSeasonListResponse =
        networkApi.getClosedSeasons(
            userId = userId,
            farmId = farmId,
            page = page
        ).getDataOrThrow()

    override suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany =
        networkApi.getCompanyByCode(code).getDataOrThrow().data

    override suspend fun getCropIncomes(userId: String, seasonId: String): List<NetworkCropIncome> =
        networkApi.getCropIncomes(
            userId = userId,
            seasonId = seasonId
        ).getDataOrThrow().data

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
        networkApi.getProductCategories().getDataOrThrow().data

    override suspend fun getFcrRecords(
        userId: String,
        seasonId: String
    ): List<NetworkFcrRecord> =
        networkApi.getFcrRecords(
            userId = userId,
            seasonId = seasonId
        ).getDataOrThrow().data

    override suspend fun getExpenseCategories(userId: String): List<NetworkExpenseCategory> =
        networkApi.getExpenseCategories(userId).getDataOrThrow().data

    override suspend fun getExpenseSubCategories(
        categoryId: String,
        userId: String
    ): List<NetworkExpenseCategory> =
        networkApi.getExpenseSubCategories(
            categoryId = categoryId,
            userId = userId
        ).getDataOrThrow().data

    override suspend fun getFishes(): List<NetworkFish> =
        networkApi.getFishes().getDataOrThrow()

    override suspend fun getProductionRecords(
        userId: String,
        seasonId: String
    ): List<NetworkProductionRecord> =
        networkApi.getProductionRecords(
            userId = userId,
            seasonId = seasonId
        ).getDataOrThrow().data

    override suspend fun getSeasonEndReasons(): List<NetworkSeasonEndReason> =
        networkApi.getSeasonEndReasons().getDataOrThrow().data

    override suspend fun getSeasonSummary(
        farmId: String,
        seasonId: String,
        userId: String
    ): NetworkSeasonSummary =
        networkApi.getSeasonSummary(
            farmId = farmId,
            seasonId = seasonId,
            userId = userId
        ).getDataOrThrow().data

    override suspend fun patchSeason(
        farmId: String,
        seasonId: String,
        userId: String,
        fields: Map<String, Any>
    ): NetworkSeason =
        networkApi.patchSeason(
            farmId = farmId,
            seasonId = seasonId,
            userId = userId,
            fields = fields
        ).getDataOrThrow().data
}