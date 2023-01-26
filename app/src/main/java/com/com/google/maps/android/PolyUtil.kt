package com.com.google.maps.android

import com.google.android.gms.maps.model.LatLng

object PolyUtil {
    fun encode(path: List<LatLng>): String? {
        var lastLat: Long = 0
        var lastLng: Long = 0
        val result = StringBuffer()
        for (latLng in path) {
            val lat = Math.round(latLng.latitude * 1e5)
            val lng = Math.round(latLng.longitude * 1e5)
            val dLat = lat - lastLat
            val dLng = lng - lastLng
            encode(dLat, result)
            encode(dLng, result)
            lastLat = lat
            lastLng = lng
        }
        return result.toString()
    }

    private fun encode(v: Long, result: StringBuffer) {
        var vv = v
        vv = if (vv < 0) (vv shl 1).inv() else vv shl 1
        while (vv >= 0x20) {
            result.append(Character.toChars((0x20 or (vv and 0x1f).toInt()) + 63))
            vv = vv shr 5
        }
        result.append(Character.toChars((vv + 63).toInt()))
    }
}