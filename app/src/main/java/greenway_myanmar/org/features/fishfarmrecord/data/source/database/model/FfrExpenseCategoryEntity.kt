package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory

@Entity(
    tableName = "ffr_expense_categories"
)
data class FfrExpenseCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo("is_harvesting")
    val isHarvesting: Boolean,
    val order: Int,
)

fun FfrExpenseCategoryEntity.asDomainModel() = ExpenseCategory(
    id = id,
    name = name,
    isHarvesting = isHarvesting
)