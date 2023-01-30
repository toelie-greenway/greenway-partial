package greenway_myanmar.org.features.fishfarmrecord.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.ApiDataWrapper
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for GreenWay Fish Farm Record Feature Network API
 */
private interface RetrofitFishFarmRecordNetworkApi {

    @GET(value = "ffr/expense-categories")
    suspend fun getExpenseCategories(): ApiResponse<ApiDataWrapper<List<NetworkExpenseCategory>>>

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
    @GET(value = "ffr/check-company")
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

    override suspend fun getExpenseCategories(): List<NetworkExpenseCategory> =
        networkApi.getExpenseCategories().getDataOrThrow().data

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

    override suspend fun getCompanyByCode(code: String): NetworkContractFarmingCompany =
        networkApi.getCompanyByCode(code).getDataOrThrow().data

}