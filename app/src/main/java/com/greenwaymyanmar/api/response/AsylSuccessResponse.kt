package com.greenwaymyanmar.api.response

import com.google.gson.annotations.SerializedName

class AsylSuccessResponse<T>(@field:SerializedName("data") val data: T)
