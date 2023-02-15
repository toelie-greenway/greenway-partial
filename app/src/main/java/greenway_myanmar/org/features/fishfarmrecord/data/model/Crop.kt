package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FFrCropEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkCrop

fun NetworkCrop.asEntity() = FFrCropEntity(
    id = id.orEmpty(),
    name = title.orEmpty(),
    iconImageUrl = image.orEmpty()
)