package com.greenwaymyanmar.common.data.api

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v2.interceptors.NoConnectivityException
import com.greenwaymyanmar.common.data.api.v2.response.AsylErrorResponse
import com.greenwaymyanmar.common.data.api.v3.NetworkDomainException
import com.greenwaymyanmar.common.data.api.v3.UnknownDomainException
import com.greenwaymyanmar.utils.errorMessage
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.common.domain.entities.Text
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
            return ApiErrorResponse(
                "Error!",
                500,
            )
//      return ApiErrorResponse(
//        ResourceError.IoMessageError(error.errorText()),
//        500
//      )
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
                val errorBody = response.errorBody()?.string()
                if (errorBody.isNullOrEmpty()) {
                    ApiErrorResponse(
                        error = response.message(),
                        code = response.code(),
                    )
                } else {
                    ApiErrorResponse(
                        error = "", //TODO:
                        code = response.code(),
                    )
                }
            }
        }
    }
}

/** separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null. */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val error: String, val code: Int) : ApiResponse<T>() {
    val errorResource: ResourceError = ResourceError.IoMessageError(
        Text.StringText(error),
    )
}

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

fun Throwable?.errorText(): Text {
//    val errorMessage: String = "ချိတ်ဆက်မှု မအောင်မြင်ပါ။\nမိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
//        if (BuildConfig.DEBUG) {
//            this?.message ?: "unknown error"
//        } else {
//            "ချိတ်ဆက်မှု မအောင်မြင်ပါ။\nမိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
//        }
    val networkError = Text.ResourceText(R.string.error_no_network)
    return when (this) {
        is UnknownDomainException -> {
            Text.StringText(this.message)
        }
        is NetworkDomainException,
        is NoConnectivityException,
        is SocketTimeoutException,
        is UnknownHostException,
        is ConnectException,
        is SSLHandshakeException -> {
            networkError
        }
        else -> {
            networkError
        }
    }
}

fun Response<*>.errorText(): Text {
    return Text.StringText(errorMessage())
}
