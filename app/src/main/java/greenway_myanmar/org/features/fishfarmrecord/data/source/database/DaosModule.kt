package greenway_myanmar.org.features.fishfarmrecord.data.source.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesFfrFarmDao(
        db: GreenWayDb,
    ): FfrFarmDao = db.ffrFarmDao()

}