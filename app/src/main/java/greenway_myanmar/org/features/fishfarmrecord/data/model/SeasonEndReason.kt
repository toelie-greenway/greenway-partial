package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEndReasonEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonEndReason

fun NetworkSeasonEndReason.asEntity() = FfrSeasonEndReasonEntity(
    id = id.orEmpty(),
    name = reason.orEmpty(),
    order = order ?: -1
)