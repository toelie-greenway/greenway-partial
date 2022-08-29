package com.greenwaymyanmar.api.response

import com.google.gson.annotations.SerializedName

class AsylDeletedResponse(
  @field:SerializedName("status_code") val statusCode: Int,
  @field:SerializedName("message") val message: String,
  @field:SerializedName("success") val isSuccessful: Boolean
)
