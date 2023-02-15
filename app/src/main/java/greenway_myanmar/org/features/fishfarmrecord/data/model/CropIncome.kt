package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCropIncomeEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkCropIncome
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkCropIncome.asEntity(seasonId: String) = FfrCropIncomeEntity(
    id = id,
    date = date.toInstantOrNow(),
    income = income.toBigDecimalOrZero(),
    crop = crop.asEntity(),
    seasonId = seasonId
)