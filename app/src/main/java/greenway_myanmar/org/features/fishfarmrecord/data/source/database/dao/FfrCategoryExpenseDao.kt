package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCategoryExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrCategoryExpenseEntity] access
 */
@Dao
abstract class FfrCategoryExpenseDao {

    @Upsert
    abstract suspend fun upsertCategoryExpenseEntities(entities: List<FfrCategoryExpenseEntity>)

    @Upsert
    abstract suspend fun upsertCategoryExpenseEntity(entity: FfrCategoryExpenseEntity)

    @Query(
        value = """
            SELECT * FROM ffr_category_expenses
            WHERE season_id = :seasonId
            ORDER BY `order` ASC
        """
    )
    abstract fun getExpensesBySeasonStream(
        seasonId: String
    ): Flow<List<FfrCategoryExpenseEntity>>

    @Query(
        value = """
            SELECT c.season_id as c_season_id, 
            e.season_id as e_season_id,
            c.category_id as c_category_id, 
            e.category_id as e_category_id,
            *
            FROM ffr_category_expenses AS c 
            JOIN ffr_expenses AS e
            ON c_season_id = e_season_id
            AND c_category_id = e_category_id
            WHERE c_category_id = :categoryId
            AND c_season_id = :seasonId
            ORDER BY `order` DESC
        """
    )
    abstract fun getCategoryExpenseStream(
        categoryId: String,
        seasonId: String
    ): Flow<Map<FfrCategoryExpenseEntity, List<FfrExpenseEntity>>?>
}