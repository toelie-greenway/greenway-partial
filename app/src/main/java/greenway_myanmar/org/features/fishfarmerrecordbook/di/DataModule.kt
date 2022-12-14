package greenway_myanmar.org.features.fishfarmerrecordbook.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.features.fishfarmerrecordbook.data.repository.fake.FakePondRepository
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.repository.PondRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun providePondRepository(repository: FakePondRepository): PondRepository
}