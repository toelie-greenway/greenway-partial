package greenway_myanmar.org.features.farmingrecord.qr.data.api

import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiFarmList
import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiQrOrder
import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiQrOrderList
import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.ApiSeasonList
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrOrderPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.ApiDataResponse
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.CreateQrOrderResponse
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.CreateQrResponse
import retrofit2.Response
import retrofit2.http.*

interface QrService {

    @GET("asymt/farms")
    suspend fun getFarmList(
        @Query("page") page: Int = 1,
        @Query("user_id") userId: Int
    ): Response<ApiFarmList>

    @GET("asymt/farms/{farm_id}/seasons")
    suspend fun getSeasonList(
        @Path("farm_id") farmId: String,
        @Query("page") page: Int? = 1,
        @Query("user_id") userId: String
    ): Response<ApiSeasonList>

    @POST("request-qr")
    suspend fun postQr(
        @Body payload: CreateQrPayload
    ): Response<ApiDataResponse<CreateQrResponse>>

    @POST("qr/order")
    suspend fun postQrOrder(
        @Body payload: CreateQrOrderPayload
    ): Response<ApiDataResponse<CreateQrOrderResponse>>

    @GET("qr/orders")
    suspend fun getQrOrders(): Response<ApiQrOrderList>

}