package com.greenwaymyanmar.common.data.api.v1.services

import com.greenwaymyanmar.common.data.api.ApiResponse
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GreenWayWebservice {

  @GET("api_path") fun getSomething(): ApiResponse<String>

  @GET("threads")
  fun getThreads(
    @Query("page") page: Int,
    @Query("limit") limit: Int,
    @Query("user_id") userId: Int,
    @QueryMap filter: Map<String, @JvmSuppressWildcards String?>
  ): Call<ThreadListResponse>
}
