package com.google.android.gms.maps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double
): Parcelable
