package com.greenwaymyanmar.common.data.api.v2.response

import com.google.gson.annotations.SerializedName

class AsylSuccessResponse<T>(@field:SerializedName("data") val data: T)
