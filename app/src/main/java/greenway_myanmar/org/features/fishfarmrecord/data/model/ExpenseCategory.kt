package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseCategoryEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory

fun NetworkExpenseCategory.asEntity() = FfrExpenseCategoryEntity(
    id = id.orEmpty(),
    name = title.orEmpty(),
    isHarvesting = is_harvesting == false,
    order = order ?: -1,
)