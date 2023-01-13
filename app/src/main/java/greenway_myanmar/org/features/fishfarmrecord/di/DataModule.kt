package greenway_myanmar.org.features.fishfarmrecord.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakeExpenseCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakeFishRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakePondRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakeSeasonRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.PondRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun providePondRepository(repository: FakePondRepository): PondRepository

    @Singleton
    @Binds
    abstract fun provideSeasonRepository(repository: FakeSeasonRepository): SeasonRepository

    @Singleton
    @Binds
    abstract fun provideExpenseCategoryRepository(repository: FakeExpenseCategoryRepository): ExpenseCategoryRepository

    @Singleton
    @Binds
    abstract fun provideFakeFishRepository(repository: FakeFishRepository): FishRepository
}