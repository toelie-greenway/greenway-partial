package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionPerFishSizeEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionPerFishTypeEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionRecordEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionPerFishSize
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionPerFishType
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkProductionRecord
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkProductionRecord.asEntity() = FfrProductionRecordEntity(
    id = id.orEmpty(),
    seasonId = season_id.orEmpty(),
    date = date.toInstantOrNow(),
    totalPrice = total.toBigDecimalOrZero(),
    note = note.orEmpty(),
    productions = productions.orEmpty().map(NetworkProductionPerFishType::asEntity)
)

fun NetworkProductionPerFishType.asEntity() = FfrProductionPerFishTypeEntity(
    fish = fish.asEntity(),
    productions = production.map(NetworkProductionPerFishSize::asEntity)
)

fun NetworkProductionPerFishSize.asEntity() = FfrProductionPerFishSizeEntity(
    size = size,
    weight = weight.toBigDecimalOrZero(),
    price = price.toBigDecimalOrZero()
)