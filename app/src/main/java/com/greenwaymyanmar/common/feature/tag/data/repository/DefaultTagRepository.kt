package com.greenwaymyanmar.common.feature.tag.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.common.feature.tag.data.source.network.TagNetworkDataSource
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.repository.TagRepository
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.di.ApplicationScope
import greenway_myanmar.org.util.kotlin.AbsentLiveData
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTagRepository @Inject constructor(
    private val network: TagNetworkDataSource,
    private val appExecutors: AppExecutors,
    private val userHelper: UserHelper,
    private val gson: Gson,
    @ApplicationScope private val externalScope: CoroutineScope,
) : TagRepository {

    override fun getTagsListing(
        categoryId: String,
        query: String?
    ): Listing<Tag> {
        val sourceFactory =
            TagDataSourceFactory(
                categoryId,
                query,
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

    override suspend fun saveThreadTagVoting(
        tagId: String,
        threadId: String,
        categoryId: String,
        previousVotedOptionId: String?
    ): Boolean {
        return withContext(externalScope.coroutineContext) {
            if (!previousVotedOptionId.isNullOrEmpty()) {
                removeThreadTagVoting(previousVotedOptionId)
            }

            if (tagId.isNotEmpty()) {
                network.postThreadTagVote(
                    tagId = tagId,
                    threadId = threadId,
                    categoryId = categoryId,
                    userId = userHelper.activeUserId.toString()
                )
                true
            } else {
                false
            }
        }
    }

    override suspend fun removeThreadTagVoting(tagVoteOptionId: String): Boolean {
        return withContext(externalScope.coroutineContext) {
            network.deleteThreadTagVote(
                tagVoteOptionId = tagVoteOptionId,
                userId = userHelper.activeUserId.toString()
            )
            true
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}