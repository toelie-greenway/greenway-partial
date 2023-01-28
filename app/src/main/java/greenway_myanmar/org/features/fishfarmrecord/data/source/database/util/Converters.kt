package greenway_myanmar.org.features.fishfarmrecord.data.source.database.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class LatLngTypeConverter @Inject constructor(
    private val json: Json
) {
    @TypeConverter
    fun stringToLatLng(value: String?): LatLng? {
        if (value.isNullOrEmpty()) return null

        return json.decodeFromString(value)
    }

    @TypeConverter
    fun latLngToString(latLng: LatLng?): String? {
        if (latLng == null) return null

        return json.encodeToString(latLng)
    }
}