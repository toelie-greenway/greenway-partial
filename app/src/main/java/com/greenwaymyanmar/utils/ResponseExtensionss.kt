package com.greenwaymyanmar.utils

import com.google.gson.Gson
import com.greenwaymyanmar.api.response.AsylErrorResponse
import greenway_myanmar.org.api.ApiError
import java.io.IOException
import retrofit2.Response
import timber.log.Timber

fun Response<*>.errorMessage(): String {

  var errorMsg: String? = null

  val errorBodyString = this.errorBody()?.string()

  if (!errorBodyString.isNullOrEmpty()) {
    try {
      val gson = Gson()
      val apiError = gson.fromJson(errorBodyString, AsylErrorResponse::class.java)
      errorMsg = apiError.message
    } catch (ignored: IOException) {
      Timber.e(ignored, "error while parsing response")
    }
  }

  if (errorMsg.isNullOrEmpty()) {
    errorMsg = this.message()
  }

  return if (!errorMsg.isNullOrEmpty()) {
    errorMsg
  } else {
    "တစ်ခုခုမှားယွင်းနေပါသည်။ ခေတ္တခဏ စောင့်ဆိုင်းပြီ ပြန်လည်ကြိုးစားကြည့်ပါ။ ကျေးဇူးတင်ပါသည်။"
  }
}

fun Response<*>.errorMessageCompat(): String {

  var errorMsg: String? = null

  val errorBodyString = this.errorBody()?.string()

  if (!errorBodyString.isNullOrEmpty()) {
    try {
      val gson = Gson()
      val apiError = gson.fromJson(errorBodyString, ApiError::class.java)
      errorMsg = apiError.error.message
    } catch (ignored: IOException) {
      Timber.e(ignored, "error while parsing response")
    }
  }

  if (errorMsg.isNullOrEmpty()) {
    errorMsg = this.message()
  }

  return if (!errorMsg.isNullOrEmpty()) {
    errorMsg
  } else {
    "တစ်ခုခုမှားယွင်းနေပါသည်။ ခေတ္တခဏ စောင့်ဆိုင်းပြီ ပြန်လည်ကြိုးစားကြည့်ပါ။ ကျေးဇူးတင်ပါသည်။"
  }
}
