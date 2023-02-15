package greenway_myanmar.org.features.fishfarmrecord.data.source.database.util;

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionPerFishTypeEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class FfrProductionPerFishTypeListConverter @Inject constructor(
    private val json: Json
) {
    @TypeConverter
    fun jsonToList(data: String?): List<FfrProductionPerFishTypeEntity>? {
        if (data == null) {
            return emptyList()
        }
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun listToJsonString(list: List<FfrProductionPerFishTypeEntity>?): String? {
        if (list.isNullOrEmpty()) return null

        return json.encodeToString(list)
    }
}