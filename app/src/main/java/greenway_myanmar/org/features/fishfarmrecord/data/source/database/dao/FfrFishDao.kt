package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFishEntity
import kotlinx.coroutines.flow.Flow


/**
 * DAO for [FfrFishEntity] access
 */
@Dao
abstract class FfrFishDao {
    @Query(
        value = """
            SELECT * FROM ffr_fishes
            WHERE name LIKE :query
            ORDER BY name ASC
        """
    )
    internal abstract fun searchFishesStream(query: String): Flow<List<FfrFishEntity>>

    fun getFishesStream(query: String): Flow<List<FfrFishEntity>> {
        return searchFishesStream("%$query%")
    }

    @Upsert
    abstract suspend fun upsertFishEntities(entities: List<FfrFishEntity>)
}