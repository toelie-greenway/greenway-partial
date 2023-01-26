package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
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
    abstract fun getFarmsStream(): Flow<FfrFarmEntity>
}