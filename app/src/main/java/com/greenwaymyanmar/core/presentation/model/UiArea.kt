package com.greenwaymyanmar.core.presentation.model

import androidx.annotation.StringRes

data class UiArea(
    val value: Double,
    val unit: String,
    @StringRes val symbolResId: Int
)