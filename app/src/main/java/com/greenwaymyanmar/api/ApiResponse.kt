/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenwaymyanmar.api

import com.google.gson.Gson
import com.greenwaymyanmar.api.interceptors.NoConnectivityException
import com.greenwaymyanmar.api.response.AsylErrorResponse
import com.greenwaymyanmar.utils.errorMessage
import greenway_myanmar.org.BuildConfig
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Common class used by API responses.
 * @param <T> the type of the response object </T>
 */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
  companion object {
    @JvmStatic
    fun <T> create(error: Throwable?): ApiErrorResponse<T> {
      val errorMessage: String =
        if (BuildConfig.DEBUG) {
          error?.message ?: "unknown error"
        } else {
          "ချိတ်ဆက်မှု မအောင်မြင်ပါ။\nမိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
        }
      return when (error) {
        is NoConnectivityException,
        is SocketTimeoutException,
        is UnknownHostException,
        is ConnectException,
        is SSLHandshakeException -> {
          ApiErrorResponse(errorMessage, 500)
        }
        else -> {
          ApiErrorResponse(errorMessage, 500)
        }
      }
    }

    @JvmStatic
    fun <T> create(response: Response<T>): ApiResponse<T> {
      return if (response.isSuccessful) {
        val body = response.body()
        if (body == null || response.code() == 204) {
          ApiEmptyResponse()
        } else {
          ApiSuccessResponse(body = body)
        }
      } else {
        ApiErrorResponse(response.errorMessage(), response.code())
      }
    }
  }
}

/** separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null. */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String, val code: Int) : ApiResponse<T>()

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
