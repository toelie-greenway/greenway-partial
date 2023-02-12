package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseCategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [FfrExpenseCategoryEntity] access
 */
@Dao
abstract class FfrExpenseCategoryDao {

    @Upsert
    abstract suspend fun upsertExpenseCategoryEntities(entities: List<FfrExpenseCategoryEntity>)

    @Upsert
    abstract suspend fun upsertExpenseCategoryEntity(entity: FfrExpenseCategoryEntity)

    @Query(
        value = """
            SELECT * FROM ffr_expense_categories
            ORDER BY `order` ASC
        """
    )
    abstract fun getExpenseCategoriesStream(): Flow<List<FfrExpenseCategoryEntity>>
}