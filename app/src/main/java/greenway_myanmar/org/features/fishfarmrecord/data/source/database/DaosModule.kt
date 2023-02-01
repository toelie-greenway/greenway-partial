package greenway_myanmar.org.features.fishfarmrecord.data.source.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesFfrFarmDao(
        db: GreenWayDb,
    ): FfrFarmDao = db.ffrFarmDao()

    @Provides
    fun providesFfrSeasonDao(
        db: GreenWayDb,
    ): FfrSeasonDao = db.ffrSeasonDao()

}