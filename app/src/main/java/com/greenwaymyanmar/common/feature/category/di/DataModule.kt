package com.greenwaymyanmar.common.feature.category.di

import com.greenwaymyanmar.common.feature.category.data.repository.DefaultCategoryRepository
import com.greenwaymyanmar.common.feature.category.domain.repository.CategoryRepository
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
    abstract fun provideCategoryRepository(
        repository: DefaultCategoryRepository
    ): CategoryRepository
}
