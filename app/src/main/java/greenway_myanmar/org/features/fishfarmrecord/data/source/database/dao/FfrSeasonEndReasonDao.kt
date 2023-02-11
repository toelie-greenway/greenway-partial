package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEndReasonEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrSeasonEndReasonEntity] access
 */
@Dao
abstract class FfrSeasonEndReasonDao {

    @Upsert
    abstract suspend fun upsertReasonsEntities(entities: List<FfrSeasonEndReasonEntity>)

    @Query(
        value = """
            SELECT * FROM ffr_season_end_reasons
            ORDER BY `order` ASC
        """
    )
    abstract fun getReasonsStream(): Flow<List<FfrSeasonEndReasonEntity>>
}