package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils

data class ApiFarmActivity(
    @SerializedName("category_id")
    val categoryId: String? = null,
    @SerializedName("category_tile")
    val categoryTile: String? = null,
    @SerializedName("expense_date")
    val expenseDate: String? = null,
    @SerializedName("inputs")
    val farmInputs: List<ApiFarmInput>? = null
) {
    fun toDomain() = FarmActivity(
        categoryId = categoryId.orEmpty(),
        categoryTitle = categoryTile.orEmpty(),
        date = DateUtils.parseIsoDateTimeToInstantOrDefault(expenseDate),
        farmInputs = farmInputs.orEmpty().map { it.toDomain() }
    )
}