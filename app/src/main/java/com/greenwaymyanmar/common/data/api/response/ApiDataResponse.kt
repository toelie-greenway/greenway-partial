package com.greenwaymyanmar.common.data.api.response

import com.google.gson.annotations.SerializedName

data class ApiDataResponse<T>(
    @SerializedName("data") val data: T
)
