package greenway_myanmar.org.features.areameasure.presentation.model

import android.content.res.Resources
import android.net.Uri
import android.os.Parcelable
import com.com.google.maps.android.PolyUtil
import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.R
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import kotlinx.parcelize.Parcelize

sealed class AreaMeasurement : Parcelable {
    @Parcelize
    data class Location(
        val latLng: LatLng,
        override val measurementType: AreaMeasureMethod
    ) : AreaMeasurement()

    @Parcelize
    data class Area(
        val coordinates: List<LatLng>,
        val acre: Double,
        override val measurementType: AreaMeasureMethod
    ) : AreaMeasurement(), Parcelable

    abstract val measurementType: AreaMeasureMethod
}

fun AreaMeasurement.getAcreOrNull() = (this as? AreaMeasurement.Area)?.acre

fun AreaMeasurement.Location.toStaticMapUrl(resources: Resources): Uri {
    val commaSeparatedLatLng = "${this.latLng.latitude},${this.latLng.longitude}"
    val urlBuilder = Uri.parse("https://maps.googleapis.com/maps/api/staticmap")
        .buildUpon()
        .appendQueryParameter("center", commaSeparatedLatLng)
        .appendQueryParameter("zoom", "17")
        .appendQueryParameter("size", "360x90") // 12:3
        .appendQueryParameter("maptype", "satellite")
        .appendQueryParameter("markers", "color:0xFFCC00|$commaSeparatedLatLng")
        .appendQueryParameter("key", resources.getString(R.string.google_maps_key))
    return urlBuilder.build()
}

fun AreaMeasurement.Area.toStaticMapUrl(resources: Resources): Uri {
    val paths = this.coordinates
    if (paths.isEmpty()) {
        return Uri.EMPTY
    }

    val completedPaths = paths.toMutableList()
    completedPaths.add(paths.first())
    val urlBuilder = Uri.parse("https://maps.googleapis.com/maps/api/staticmap")
        .buildUpon()
        .appendQueryParameter("zoom", "17")
        .appendQueryParameter("size", "360x90") // 12:3
        .appendQueryParameter("maptype", "satellite")
        .appendQueryParameter(
            "path",
            "color:0xFFCC00|weight:5|fillcolor:white|enc:${PolyUtil.encode(completedPaths.toList())}",
        )
        .appendQueryParameter("key", resources.getString(R.string.google_maps_key))
    return urlBuilder.build()
}
