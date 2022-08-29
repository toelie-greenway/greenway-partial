package com.greenwaymyanmar.api.interceptors

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import greenway_myanmar.org.util.NetworkManager
import java.io.IOException
import javax.inject.Inject


class NetworkStatusInterceptor

class NoConnectivityException : IOException() {
    override val message: String
        get() =
            "ချိတ်ဆက်မှု မအောင်မြင်ပါ။\nမိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
}
