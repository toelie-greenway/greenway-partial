package greenway_myanmar.org.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.features.farmingrecord.qr.data.repositories.DefaultFarmRepository
import greenway_myanmar.org.features.farmingrecord.qr.data.repositories.DefaultQrRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.QrRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

  @Binds
  @Singleton
  abstract fun bindFarmRepository(repository: DefaultFarmRepository): FarmRepository

  @Binds
  @Singleton
  abstract fun bindQrRepository(repository: DefaultQrRepository): QrRepository
}
