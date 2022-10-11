package com.greenwaymyanmar.common.data.api.v2.response

import com.google.gson.annotations.SerializedName

data class AsylErrorResponse(
  @SerializedName("message") val message: String,
  @SerializedName("status_code") val statusCode: Int,
  @SerializedName("success") val isSuccessful: Boolean
)
