package greenway_myanmar.org.repository

import androidx.annotation.StringDef
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.repository.tag.PostByTagDataSourceFactory
import greenway_myanmar.org.util.RateLimiter
import greenway_myanmar.org.util.kotlin.AbsentLiveData
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Post
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: GreenWayDb,
    private val webservice: GreenWayWebservice,
    private val gson: Gson,
    private val userHelper: UserHelper
) {
    // TODO: set rateLimit about 10 in production
    private val postListRateLimit = RateLimiter<String>(5, TimeUnit.MINUTES)
    private val postRateLimit = RateLimiter<String>(5, TimeUnit.MINUTES)
    private val weatherPostRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun getPostsByTag(): Listing<Post> {
        val sourceFactory =
            PostByTagDataSourceFactory(
                webservice,
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
                sourceFactory.requestNextPage()
            }
        )
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(FEEDBACK_TYPE_LIKE, FEEDBACK_TYPE_DISLIKE)
    annotation class FeedbackType
    companion object {
        const val FEEDBACK_TYPE_LIKE = "useful"
        const val FEEDBACK_TYPE_DISLIKE = "not_useful"
        private const val PAGE_SIZE = 10
    }
}