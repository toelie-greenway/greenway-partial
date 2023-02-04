package greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrEntity


/**
 * DAO for [FfrFcrEntity] access
 */
@Dao
abstract class FfrFcrDao {

    @Upsert
    abstract suspend fun upsertFcrEntities(entities: List<FfrFcrEntity>)
}