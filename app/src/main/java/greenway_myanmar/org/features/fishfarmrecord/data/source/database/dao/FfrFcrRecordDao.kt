package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.PopulatedFcrRecord
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrFcrRecordEntity] access
 */
@Dao
abstract class FfrFcrRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFcrRecord(entity: FfrFcrRecordEntity): Long

    @Upsert
    abstract suspend fun upsertFcrRecords(entities: List<FfrFcrRecordEntity>)

    @Upsert
    abstract suspend fun upsertFcrRecord(entity: FfrFcrRecordEntity)

    @Query("SELECT * FROM ffr_fcr_records WHERE rowid = :rowId")
    abstract suspend fun getFcrRecordByRowId(rowId: Long): FfrFcrRecordEntity

    @Query(
        value = """
            SELECT * FROM ffr_fcr_records
            WHERE season_id = :seasonId
            ORDER BY date DESC
        """
    )
    @Transaction
    abstract fun getFcrRecordsStream(
        seasonId: String
    ): Flow<List<PopulatedFcrRecord>>
}