package greenway_myanmar.org.features.fishfarmrecord.data.source.database.util

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LatLngConverter {
    @TypeConverter
    fun stringToLatLng(value: String?): LatLng? {
        if (value.isNullOrEmpty()) return null

        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun latLngToString(latLng: LatLng?): String? {
        if (latLng == null) return null

        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.encodeToString(latLng)
    }
}
//
//class NewsResourceTypeConverter {
//    @TypeConverter
//    fun newsResourceTypeToString(value: NewsResourceType?): String? =
//        value?.let(NewsResourceType::serializedName)
//
//    @TypeConverter
//    fun stringToNewsResourceType(serializedName: String?): NewsResourceType =
//        serializedName.asNewsResourceType()
//}
