package com.greenwaymyanmar.api.response

import com.google.gson.annotations.SerializedName

data class AsylErrorResponse(
  @SerializedName("message") val message: String,
  @SerializedName("status_code") val statusCode: Int,
  @SerializedName("success") val isSuccessful: Boolean
)
