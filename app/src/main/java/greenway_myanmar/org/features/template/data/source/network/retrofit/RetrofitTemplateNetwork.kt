package greenway_myanmar.org.features.template.data.source.network.retrofit

import com.greenwaymyanmar.common.data.api.v3.ApiResponse
import com.greenwaymyanmar.common.data.api.v3.getDataOrThrow
import greenway_myanmar.org.features.template.data.source.network.TemplateNetworkDataSource
import greenway_myanmar.org.features.template.data.source.network.model.ApiDataWrapper
import greenway_myanmar.org.features.template.data.source.network.model.NetworkImage
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Retrofit API declaration for GreenWay Fish Farm Record Feature Network API
 */
private interface RetrofitTemplateNetworkApi {

    @Multipart
    @POST("upload")
    suspend fun postImage(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody?>
    ): ApiResponse<ApiDataWrapper<NetworkImage>>

}

/**
 * [Retrofit] backed [TemplateNetworkDataSource]
 */
@Singleton
class RetrofitTemplateNetworkDataSource @Inject constructor(
    retrofit: Retrofit
) : TemplateNetworkDataSource {

    private val networkApi: RetrofitTemplateNetworkApi =
        retrofit.create(RetrofitTemplateNetworkApi::class.java)

    override suspend fun postImage(params: Map<String, RequestBody?>): NetworkImage =
        networkApi.postImage(params).getDataOrThrow().data

}