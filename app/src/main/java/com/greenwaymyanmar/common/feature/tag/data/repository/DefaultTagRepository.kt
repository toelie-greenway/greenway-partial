package com.greenwaymyanmar.common.feature.tag.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.repository.TagRepository
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.util.kotlin.AbsentLiveData
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTagRepository @Inject constructor(
    private val network: TagNetworkDataSource,
    private val appExecutors: AppExecutors,
    private val gson: Gson,
) : TagRepository {

    override fun getTagsListing(): Listing<Tag> {
        val sourceFactory =
            TagDataSourceFactory(
                network,
                appExecutors,
                gson,
                PAGE_SIZE
            )
        val builder = LivePagedListBuilder(sourceFactory, PAGE_SIZE)
        val networkState =
            Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState }
        val itemCount = Transformations.switchMap(sourceFactory.sourceLiveData) { it.itemCount }
        val hasMore = Transformations.switchMap(sourceFactory.sourceLiveData) { it.hasMore }

        val refreshTrigger = MutableLiveData<Void>()
        val refreshState =
            Transformations.switchMap(refreshTrigger) { AbsentLiveData.create<NetworkState?>() }
        return Listing(
            builder.build(),
            itemCount,
            networkState,
            refreshState,
            hasMore,
            {
                sourceFactory.refresh()
            },
            {
                sourceFactory.retry()
            },
            {
                /* no-op */
            }
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}