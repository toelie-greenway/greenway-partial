package greenway_myanmar.org.features.template.data.source.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import greenway_myanmar.org.features.template.data.source.database.model.TemplateEntity


/**
 * DAO for [TemplateEntity] access
 */
@Dao
abstract class TemplateDao {

    @Upsert
    abstract suspend fun upsertEntities(entities: List<TemplateEntity>)
}