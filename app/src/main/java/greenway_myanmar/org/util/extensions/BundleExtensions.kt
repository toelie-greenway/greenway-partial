package greenway_myanmar.org.util.extensions

import android.content.Intent
import android.os.Build
import android.os.Bundle

// source: https://issuetracker.google.com/issues/243986223
inline fun <reified T> Intent.getParcelableExtraCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T?
}

inline fun <reified T> Bundle.getParcelableExtraCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T?
}