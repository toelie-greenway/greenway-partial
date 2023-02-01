package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [FfrSeasonEntity] access
 */
@Dao
abstract class FfrSeasonDao {

    @Query(
        value = """
            SELECT * FROM ffr_seasons
        """
    )
    abstract fun getSeasonsStream(): Flow<List<FfrSeasonEntity>>

    @Query(
        value = """
            SELECT * FROM ffr_seasons
            WHERE id = :id
        """
    )
    abstract suspend fun getSeasonById(id: String): FfrSeasonEntity?

    @Upsert
    abstract suspend fun upsertSeason(entity: FfrSeasonEntity)
}