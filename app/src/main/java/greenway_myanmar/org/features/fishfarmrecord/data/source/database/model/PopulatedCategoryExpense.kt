package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class PopulatedCategoryExpense(
    @Embedded
    val categoryExpense: FfrCategoryExpenseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "record_id"
    )
    val expenses: List<FfrExpenseEntity>
)

//fun PopulatedCategoryExpense.asDomainModel() = CategoryExpense(
//    category = categoryExpense.asDomainModel()
//    id = fcrRecord.id,
//    date = fcrRecord.date,
//    ratios = ratios.map(FfrFcrEntity::asDomainModel)
//)