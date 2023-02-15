package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Crop

data class FFrCropEntity(
    val id: String,
    val name: String,
    @ColumnInfo("icon_image_url")
    val iconImageUrl: String
)

fun FFrCropEntity.asDomainModel() = Crop(
    id = id,
    name = name,
    iconImageUrl = iconImageUrl
)