package greenway_myanmar.org.features.fishfarmrecord.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.features.fishfarmrecord.data.repository.DefaultContractFarmingCompanyRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.DefaultExpenseCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.DefaultFarmInputProductCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.DefaultFarmInputProductRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.DefaultFarmRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakeFishRepository
import greenway_myanmar.org.features.fishfarmrecord.data.repository.fake.FakeSeasonRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ContractFarmingCompanyRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductCategoryRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideFarmRepository(repository: DefaultFarmRepository): FarmRepository

    @Singleton
    @Binds
    abstract fun provideSeasonRepository(repository: FakeSeasonRepository): SeasonRepository

    @Singleton
    @Binds
    abstract fun provideExpenseCategoryRepository(
        repository: DefaultExpenseCategoryRepository
    ): ExpenseCategoryRepository

    @Singleton
    @Binds
    abstract fun provideFarmInputProductRepository(
        repository: DefaultFarmInputProductRepository
    ): FarmInputProductRepository

    @Singleton
    @Binds
    abstract fun provideFarmInputProductCategoryRepository(
        repository: DefaultFarmInputProductCategoryRepository
    ): FarmInputProductCategoryRepository

    @Singleton
    @Binds
    abstract fun provideFakeFishRepository(repository: FakeFishRepository): FishRepository

    @Singleton
    @Binds
    abstract fun provideContractFarmingCompanyRepository(repository: DefaultContractFarmingCompanyRepository): ContractFarmingCompanyRepository
}