package greenway_myanmar.org.features.template.data.source.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.features.template.data.source.database.dao.TemplateDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesTemplateDao(
        db: GreenWayDb,
    ): TemplateDao = db.templateDao()

}