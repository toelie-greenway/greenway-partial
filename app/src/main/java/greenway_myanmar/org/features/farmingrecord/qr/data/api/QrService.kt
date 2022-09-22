package greenway_myanmar.org.features.farmingrecord.qr.data.api

import greenway_myanmar.org.features.farmingrecord.qr.data.api.model.*
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateQrOrderPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.payloads.CreateUpdateQrPayload
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.ApiDataResponse
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.CreateQrOrderResponse
import greenway_myanmar.org.features.farmingrecord.qr.data.api.responses.CreateUpdateQrResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface QrService {

    @GET("asymt/farms")
    fun getFarmList(
        @Query("page") page: Int = 1,
        @Query("user_id") userId: Int
    ): Call<ApiFarmList>

    @GET("asymt/farms/{farm_id}/seasons")
    suspend fun getSeasonList(
        @Path("farm_id") farmId: String,
        @Query("page") page: Int? = 1,
        @Query("user_id") userId: String
    ): Response<ApiSeasonList>

    @POST("request-qr")
    suspend fun postQr(
        @Body payload: CreateUpdateQrPayload
    ): Response<ApiDataResponse<CreateUpdateQrResponse>>

    @PATCH("/qr/request-qr/{qr_id}/iupdate")
    suspend fun updateQr(
        @Path("qr_id") qrId: String,
        @Body payload: CreateUpdateQrPayload
    ): Response<ApiDataResponse<CreateUpdateQrResponse>>

    @POST("qr/order")
    suspend fun postQrOrder(
        @Body payload: CreateQrOrderPayload
    ): Response<ApiDataResponse<CreateQrOrderResponse>>

    @GET("qr/orders")
    suspend fun getQrOrders(): Response<ApiQrOrderList>

    @GET("qr/order/{order_id}/statues")
    suspend fun getOrder(@Path("order_id") id: String): Response<ApiDataResponse<ApiQrOrder>>

    @GET("qr-quantities")
    suspend fun getQuantities(): Response<ApiQrQuantityListResponse>

    @GET("qr/{qr_id}")
    suspend fun getQrDetail(@Path("qr_id") qrId: String): Response<ApiDataResponse<ApiQr>>
}