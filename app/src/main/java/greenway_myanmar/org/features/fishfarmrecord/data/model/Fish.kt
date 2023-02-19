package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFishEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonFishType
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish

fun NetworkSeasonFishType.asEntity() = FfrFishEntity(
    id = id.orEmpty(),
    name = fish_detail?.name.orEmpty(),
    species = specie,
    iconImageUrl = fish_detail?.image
)

fun NetworkSeasonFishType.asDomainModel() = Fish(
    id = id.orEmpty(),
    name = fish_detail?.name.orEmpty(),
    species = specie.orEmpty(),
    iconImageUrl = fish_detail?.image.orEmpty()
)

fun NetworkFish.asEntity() = FfrFishEntity(
    id = if (id.isNullOrEmpty()) throw IllegalStateException("id must not be null") else id,
    name = name.orEmpty(),
    iconImageUrl = image
)