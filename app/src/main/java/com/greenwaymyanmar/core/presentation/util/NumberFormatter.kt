package com.greenwaymyanmar.core.presentation.util

import greenway_myanmar.org.util.MyanmarZarConverter
import java.text.NumberFormat

val numberFormat: NumberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())
