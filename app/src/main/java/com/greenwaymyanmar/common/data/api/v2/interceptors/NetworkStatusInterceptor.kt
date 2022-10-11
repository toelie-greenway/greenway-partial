package com.greenwaymyanmar.common.data.api.v2.interceptors

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import greenway_myanmar.org.util.NetworkManager
import java.io.IOException
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkStatusInterceptor
@Inject
constructor(@ApplicationContext private val context: Context) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    if (NetworkManager.isOnline(context)) {
      return chain.proceed(chain.request())
    } else {
      throw NoConnectivityException()
    }
  }
}

class NoConnectivityException : IOException() {
  override val message: String
    get() =
      "ချိတ်ဆက်မှု မအောင်မြင်ပါ။\nမိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
}
