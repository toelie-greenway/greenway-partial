package com.greenwaymyanmar.common.feature.tag.data.source.network.di

import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.data.source.network.retrofit.RetrofitTagNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

  @Binds
  fun bindsTagNetworkDataSource(
    network: RetrofitTagNetworkDataSource
  ): TagNetworkDataSource
}
