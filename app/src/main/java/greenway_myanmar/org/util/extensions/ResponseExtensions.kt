package greenway_myanmar.org.util.extensions

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.ApiError
import com.greenwaymyanmar.common.data.api.v2.response.AsylErrorResponse
import greenway_myanmar.org.BuildConfig
import retrofit2.Response
import timber.log.Timber

fun Response<*>.errorMessage(): String {

  var errorMsg: String? = null

  val errorBodyString = this.errorBody()?.string()

  if (!errorBodyString.isNullOrEmpty()) {
    errorMsg = parseApiV1ErrorMessage(errorBodyString)

    if (errorMsg.isEmpty()) {
      errorMsg = parseApiV2ErrorMessage(errorBodyString)
    }
  }

  if (errorMsg.isNullOrEmpty() && BuildConfig.DEBUG) {
    errorMsg = this.message()
  }

  return if (!errorMsg.isNullOrEmpty()) {
    errorMsg
  } else {
    "တစ်ခုခုမှားယွင်းနေပါသည်။ ခေတ္တခဏ စောင့်ဆိုင်းပြီ ပြန်လည်ကြိုးစားကြည့်ပါ။ ကျေးဇူးတင်ပါသည်။"
  }
}

private fun parseApiV1ErrorMessage(errorBodyString: String): String {
  return try {
    val gson = Gson()
    val apiError = gson.fromJson(errorBodyString, ApiError::class.java)
    apiError?.error?.message.orEmpty()
  } catch (ignored: Exception) {
    Timber.e(ignored, "error while parsing response")
    ""
  }
}

private fun parseApiV2ErrorMessage(errorBodyString: String): String {
  return try {
    val gson = Gson()
    val apiError = gson.fromJson(errorBodyString, AsylErrorResponse::class.java)
    apiError?.message.orEmpty()
  } catch (ignored: Exception) {
    Timber.e(ignored, "error while parsing response")
    ""
  }
}
