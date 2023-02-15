package greenway_myanmar.org.features.fishfarmrecord.data.source.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrCategoryExpenseDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrCropIncomeDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrExpenseCategoryDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrExpenseDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFishDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrProductionRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonEndReasonDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesFfrCategoryExpenseDao(
        db: GreenWayDb,
    ): FfrCategoryExpenseDao = db.ffrCategoryExpenseDao()

    @Provides
    fun providesFfrCropIncomeDao(
        db: GreenWayDb,
    ): FfrCropIncomeDao = db.ffrCropIncomeDao()

    @Provides
    fun providesFfrExpenseDao(
        db: GreenWayDb,
    ): FfrExpenseDao = db.ffrExpenseDao()

    @Provides
    fun providesFfrExpenseCategoryDao(
        db: GreenWayDb,
    ): FfrExpenseCategoryDao = db.ffrExpenseCategoryDao()

    @Provides
    fun providesFfrFarmDao(
        db: GreenWayDb,
    ): FfrFarmDao = db.ffrFarmDao()

    @Provides
    fun providesFfrFishDao(
        db: GreenWayDb,
    ): FfrFishDao = db.ffrFishDao()

    @Provides
    fun providesFfrSeasonDao(
        db: GreenWayDb,
    ): FfrSeasonDao = db.ffrSeasonDao()

    @Provides
    fun providesFfrFcrDao(
        db: GreenWayDb,
    ): FfrFcrDao = db.ffrFcrDao()

    @Provides
    fun providesFfrFcrRecordDao(
        db: GreenWayDb,
    ): FfrFcrRecordDao = db.ffrFcrRecordDao()

    @Provides
    fun providesFfrProductionRecordDao(
        db: GreenWayDb,
    ): FfrProductionRecordDao = db.ffrProductionRecordDao()

    @Provides
    fun providesFfrSeasonEndReasonDao(
        db: GreenWayDb,
    ): FfrSeasonEndReasonDao = db.ffrSeasonEndReasonDao()

}