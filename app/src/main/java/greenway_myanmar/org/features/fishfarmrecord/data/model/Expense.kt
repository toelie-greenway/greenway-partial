package greenway_myanmar.org.features.fishfarmrecord.data.model

import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmInputExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpense
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputExpense
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.extensions.toBigDecimalOrNull
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkExpense.asEntity(
    seasonId: String,
    categoryId: String
) = FfrExpenseEntity(
    id = id,
    date = date.toInstantOrNow(),
    labourQuantity = labour_qty,
    labourCost = labour_cost.toBigDecimalOrNull(),
    familyQuantity = family_qty,
    familyCost = family_cost.toBigDecimalOrNull(),
    machineryCost = machinery_cost.toBigDecimalOrNull(),
    inputs = inputs.orEmpty().map(NetworkFarmInputExpense::asEntity),
    photos = photos,
    remark = remark,
    seasonId = seasonId,
    categoryId = categoryId
)

fun NetworkFarmInputExpense.asEntity() = FfrFarmInputExpenseEntity(
    productId = product_id.orEmpty(),
    productName = product_name.orEmpty(),
    productThumbnail = product_thumbnail.orEmpty(),
    amount = quantity.orZero(),
    unit = unit.orEmpty(),
    unitPrice = unit_price.orZero(),
    fingerlingWeight = estimated_weight,
    fingerlingSize = estimated_size,
    fingerlingAge = fingerling_age,
    totalCost = total_cost.orZero()
)