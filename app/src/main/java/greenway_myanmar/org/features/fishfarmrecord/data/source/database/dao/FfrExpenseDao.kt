package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrExpenseEntity] access
 */
@Dao
abstract class FfrExpenseDao {

    @Upsert
    abstract suspend fun upsertExpenseEntities(entities: List<FfrExpenseEntity>)

    @Upsert
    abstract suspend fun upsertExpenseEntity(entity: FfrExpenseEntity)

    @Query(
        value = """
            SELECT * FROM ffr_expenses
            WHERE category_id = :categoryId
            AND season_id = :seasonId
            ORDER BY date DESC
        """
    )
    abstract fun getExpensesByCategoryStream(
        categoryId: String,
        seasonId: String
    ): Flow<List<FfrExpenseEntity>>
}