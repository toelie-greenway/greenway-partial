package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.PopulatedFcrRecord
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET


/**
 * DAO for [FfrFcrRecordEntity] access
 */
@Dao
abstract class FfrFcrRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFcrRecord(entity: FfrFcrRecordEntity): Long

    @Upsert
    abstract suspend fun upsertFcrRecord(entity: FfrFcrRecordEntity)

    @GET("SELECT * FROM ffr_fcr_record WHERE rowid = :rowId")
    abstract suspend fun getFcrRecordByRowId(rowId: Long): FfrFcrRecordEntity

    @GET(
        value = """
            SELECT * FROM ffr_fcr_record
            ORDER BY date DESC
        """
    )
    @Transaction
    abstract fun getFcrRecordsStream(): Flow<PopulatedFcrRecord>
}