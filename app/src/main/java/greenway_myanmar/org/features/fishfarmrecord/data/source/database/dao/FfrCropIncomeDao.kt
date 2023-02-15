package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCropIncomeEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrCropIncomeEntity] access
 */
@Dao
abstract class FfrCropIncomeDao {

    @Upsert
    abstract suspend fun upsertCropIncomes(entities: List<FfrCropIncomeEntity>)

    @Upsert
    abstract suspend fun upsertCropIncome(entity: FfrCropIncomeEntity)

    @Query(
        value = """
            SELECT * FROM ffr_crop_incomes
            WHERE season_id = :seasonId
            ORDER BY date DESC
        """
    )
    abstract fun getCropIncomesStream(
        seasonId: String
    ): Flow<List<FfrCropIncomeEntity>>
}