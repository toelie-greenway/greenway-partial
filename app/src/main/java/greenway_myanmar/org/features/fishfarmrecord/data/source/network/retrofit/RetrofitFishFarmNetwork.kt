package greenway_myanmar.org.features.fishfarmrecord.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.ApiDataWrapper
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for GreenWay Fish Farm Record Feature Network API
 */
private interface RetrofitFishFarmRecordNetworkApi {

    @GET(value = "ffr/expense-categories")
    suspend fun getExpenseCategories(): ApiResponse<ApiDataWrapper<List<NetworkExpenseCategory>>>

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

}