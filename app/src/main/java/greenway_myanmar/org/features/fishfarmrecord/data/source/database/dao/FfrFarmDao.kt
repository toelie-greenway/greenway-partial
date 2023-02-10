package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.PopulatedFarm
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [FfrFarmEntity] access
 */
@Dao
abstract class FfrFarmDao {

    @Query(
        value = """
            SELECT * FROM ffr_farms
            ORDER BY opening_season_id IS NOT NULL DESC, created_at DESC, name ASC
        """
    )
    @Transaction
    abstract fun getFarmsStream(): Flow<List<PopulatedFarm>>

    @Upsert
    abstract suspend fun upsertFarm(entity: FfrFarmEntity)

    @Upsert
    abstract suspend fun upsertFarms(entities: List<FfrFarmEntity>)

    @Query(
        value = """
            SELECT * FROM ffr_farms
            WHERE id = :id
        """
    )
    abstract suspend fun getFarmById(id: String): FfrFarmEntity

    @Query(
        value = """
            SELECT * FROM ffr_farms
            WHERE opening_season_id = :seasonId
        """
    )
    abstract suspend fun getFarmBySeasonId(seasonId: String): FfrFarmEntity?

    @Query(
        value = """
            SELECT * FROM ffr_farms
            WHERE id = :id
        """
    )
    @Transaction
    abstract fun getFarmStreamById(id: String): Flow<PopulatedFarm?>

    @Query(
        value = """
            DELETE FROM ffr_farms
            WHERE id = :id
        """
    )
    abstract suspend fun deleteFarmById(id: String)

    @Query(
        value = """
            UPDATE ffr_farms 
            SET opening_season_id = :seasonId
            WHERE id = :farmId
        """
    )
    abstract suspend fun updateSeasonId(farmId: String, seasonId: String)
}