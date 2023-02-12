package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Entity(
    tableName = "ffr_category_expenses",
    primaryKeys = ["category_id", "season_id"]
)
data class FfrCategoryExpenseEntity(
    @ColumnInfo("category_id")
    val categoryId: String,
    @ColumnInfo("category_name")
    val categoryName: String,
    @ColumnInfo("is_harvesting")
    val isHarvesting: Boolean,
    val order: Int,
    @ColumnInfo(name = "total_expense")
    val totalExpenses: BigDecimal,
    @ColumnInfo(name = "last_cost_created_date")
    val lastRecordDate: Instant? = null,
    @ColumnInfo(name = "season_id")
    val seasonId: String
)

fun FfrCategoryExpenseEntity.asDomainModel(expenses: List<FfrExpenseEntity> = emptyList()) = CategoryExpense(
    category = ExpenseCategory(
        id = categoryId,
        name = categoryName,
        isHarvesting = isHarvesting,
    ),
    totalExpenses = totalExpenses,
    lastRecordDate = lastRecordDate,
    expenses = expenses.map(FfrExpenseEntity::asDomainModel)
)