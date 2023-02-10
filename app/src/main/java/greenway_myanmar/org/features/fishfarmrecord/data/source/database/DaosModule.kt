package greenway_myanmar.org.features.fishfarmrecord.data.source.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFishDao
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

    @Provides
    fun providesFfrFcrRecordDao(
        db: GreenWayDb,
    ): FfrFcrRecordDao = db.ffrFcrRecordDao()

    @Provides
    fun providesFfrFcrDao(
        db: GreenWayDb,
    ): FfrFcrDao = db.ffrFcrDao()

    @Provides
    fun providesFfrFishDao(
        db: GreenWayDb,
    ): FfrFishDao = db.ffrFishDao()
}