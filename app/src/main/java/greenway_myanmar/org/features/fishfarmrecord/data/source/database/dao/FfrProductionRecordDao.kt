package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionRecordEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrProductionRecordEntity] access
 */
@Dao
abstract class FfrProductionRecordDao {

    @Upsert
    abstract suspend fun upsertProductionRecords(entities: List<FfrProductionRecordEntity>)

    @Upsert
    abstract suspend fun upsertProductionRecord(entity: FfrProductionRecordEntity)

    @Query(
        value = """
            SELECT * FROM ffr_production_records
            WHERE season_id = :seasonId
            ORDER BY date DESC
        """
    )
    abstract fun getProductionRecordsStream(
        seasonId: String
    ): Flow<List<FfrProductionRecordEntity>>
}