package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCategoryExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkCategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputExpense
import greenway_myanmar.org.util.extensions.toBigDecimalOrNull
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow
import greenway_myanmar.org.util.toInstantOrNull
import java.math.BigDecimal

fun NetworkCategoryExpense.asEntity(
    seasonId: String
) = FfrCategoryExpenseEntity(
    categoryId = id.orEmpty(),
    categoryName = title.orEmpty(),
    isHarvesting = is_harvesting ?: false,
    order = order ?: -1,
    totalExpenses = if (total_cost == null) BigDecimal.ZERO else BigDecimal.valueOf(total_cost),
    lastRecordDate = last_cost_created_date.toInstantOrNull(),
    seasonId = seasonId
)

fun NetworkCategoryExpense.asExpenseEntities(
    seasonId: String,
    categoryId: String
) = this.expenses.orEmpty().map {
    FfrExpenseEntity(
        id = it.id,
        seasonId = if (it.season_id.isNullOrEmpty()) seasonId else it.season_id,
        categoryId = it.category?.id ?: categoryId,
        date = it.date.toInstantOrNow(),
        labourQuantity = it.labour_qty,
        labourCost = it.labour_cost.toBigDecimalOrNull(),
        familyQuantity = it.family_qty,
        familyCost = it.family_cost.toBigDecimalOrNull(),
        machineryCost = it.machinery_cost.toBigDecimalOrNull(),
        totalCost = it.total_cost.toBigDecimalOrZero(),
        inputs = it.inputs.orEmpty().map(NetworkFarmInputExpense::asEntity),
        photos = it.photos,
        remark = it.remark
    )
}