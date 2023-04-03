package com.greenwaymyanmar.common.feature.tag.di

import com.greenwaymyanmar.common.feature.tag.data.repository.DefaultTagRepository
import com.greenwaymyanmar.common.feature.tag.domain.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideTagRepository(
        repository: DefaultTagRepository
    ): TagRepository
}
