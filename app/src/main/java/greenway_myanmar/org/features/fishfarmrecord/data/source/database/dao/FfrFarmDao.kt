package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [FfrFarmEntity] access
 */
@Dao
abstract class FfrFarmDao {

    @Query(
        value = """
            SELECT * FROM ffr_farms
        """
    )
    abstract fun getFarmsStream(): Flow<List<FfrFarmEntity>>

    @Upsert
    abstract suspend fun upsertFarm(entity: FfrFarmEntity)

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
            WHERE id = :id
        """
    )
    abstract fun getFarmStreamById(id: String): Flow<FfrFarmEntity>

    @Query(
        value = """
            DELETE FROM ffr_farms
            WHERE id = :id
        """
    )
    abstract suspend fun deleteFarmById(id: String)

}