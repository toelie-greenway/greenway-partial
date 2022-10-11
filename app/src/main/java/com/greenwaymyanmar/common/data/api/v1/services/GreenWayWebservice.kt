package com.greenwaymyanmar.common.data.api.v1.services

import androidx.lifecycle.LiveData
import com.greenwaymyanmar.common.data.api.ApiResponse
import retrofit2.http.GET

interface GreenWayWebservice {

  @GET("api_path") fun getSomething(): ApiResponse<String>

}
