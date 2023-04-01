package greenway_myanmar.org.repository

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import dagger.hilt.android.qualifiers.ApplicationContext
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.repository.boundarycallback.GreenWayPagedListBoundaryCallback
import greenway_myanmar.org.repository.boundarycallback.ThreadManualBoundaryCallback
import greenway_myanmar.org.ui.threads.ThreadsViewModel
import greenway_myanmar.org.util.PaginationUtils
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Thread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ThreadRepository
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val appExecutors: AppExecutors,
    private val webservice: GreenWayWebservice,
    private val db: GreenWayDb,
    private val userHelper: UserHelper,
    private val gson: Gson
) {
    /*
     When refresh is called, we simply run a fresh network request and when it arrives, clear
     the database table and insert all new items in a transaction.
     <p>
     Since the PagedList already uses a database bound data source, it will automatically be
     updated after the database transaction is finished.
    */
    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear the
     * database table and insert all new items in a transaction.
     *
     * Since the PagedList already uses a database bound data source, it will automatically be updated
     * after the database transaction is finished.
     */
    @MainThread
    private fun refresh(hasLimit: Boolean, query: ThreadsViewModel.Query): LiveData<NetworkState> {
        Timber.d("refresh()")
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        // create call according to having limit or not
        val refreshCall: Call<ThreadListResponse> =
            webservice.getThreads(
                1,
                DEFAULT_PAGE_SIZE,
                userHelper.activeUserId,
                query.toApiQueryMap()
            )

        refreshCall.enqueue(
            object : Callback<ThreadListResponse?> {
                override fun onResponse(
                    call: Call<ThreadListResponse?>,
                    response: Response<ThreadListResponse?>
                ) {
                    appExecutors.diskIO().execute {
                        db.runInTransaction {
                            db.threadDao().delete()
                            if (response.body() != null) {
                                insertResultIntoDb(response.body(), query)
                            }
                        }
                    }
                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }

                override fun onFailure(call: Call<ThreadListResponse?>, t: Throwable) {
                    // retrofit calls this on main thread so safe to call set value
                    networkState.value = NetworkState.error(t.message)
                }
            },
        )
        return networkState
    }

    /** Returns a Listing of threads. */
    @MainThread
    fun loadThreads(query: ThreadsViewModel.Query): Listing<Thread> {
        appExecutors.diskIO().execute {
            db.threadDao().delete()
            db.paginationDao().deleteByResourceType(Pagination.RESOURCE_TYPE_THREAD)
        }

        Timber.d("loadThreads()")
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback =
            ThreadManualBoundaryCallback(
                appExecutors,
                db,
                webservice,
                DEFAULT_PAGE_SIZE,
                object : GreenWayPagedListBoundaryCallback.DataResponseCallback<ThreadListResponse> {
                    override fun handleResponse(response: ThreadListResponse?) {
                        insertResultIntoDb(response, query)
                    }
                },
                userHelper,
                query,
            )

        // create a data source factory from Room
        val filterMap = query.toDbQueryMap()
        val selectionStringBuilder = StringBuilder()
        val selectionArgs: Array<String?> = arrayOfNulls(filterMap?.size ?: 0)
        var selectionIndex = 0
        filterMap?.forEach { filter ->
            if (selectionStringBuilder.isNotEmpty()) {
                selectionStringBuilder.append(" AND ")
            }
            selectionStringBuilder.append(filter.key)
            selectionStringBuilder.append(" = ")
            selectionStringBuilder.append(" ? ")
            selectionArgs[selectionIndex++] = filter.value
        }

        val sqliteQuery =
            SupportSQLiteQueryBuilder.builder("Thread")
                .selection(selectionStringBuilder.toString(), selectionArgs)
                .orderBy("createdAt DESC")
                .create()

        // create a data source factory from Room
        val dataSourceFactory = db.threadDao().loadThreads()

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(DEFAULT_PAGE_SIZE).build()

        val builder =
            LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                .setBoundaryCallback(boundaryCallback)

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Void?>()
        val refreshState =
            Transformations.switchMap(refreshTrigger) { input: Void? -> refresh(false, query) }
        return Listing(
            builder.build(),
            boundaryCallback.networkState,
            refreshState,
            boundaryCallback.hasMore,
            { refreshTrigger.setValue(null) },
            { boundaryCallback.pagingRequestHelper.retryAllFailed() },
            { boundaryCallback.requestNextPage()}
        )
    }


    private fun insertResultIntoDb(threads: List<Thread>) {
        // save threads
        db.threadDao().insert(threads)

        // save thread owner
        for ((_, _, _, _, _, user) in threads) {
            if (user != null) {
                // db.userDao().insert(thread.getUser());
                //userRepository.insertUserIntoDb(user)
            }
        }
    }

    private fun insertResultIntoDb(response: ThreadListResponse?, query: ThreadsViewModel.Query) {
        if (response == null || response.threads == null) return
        db.runInTransaction {

            // save threads
            insertResultIntoDb(response.threads)

            // save pagination
            val pagination = response.pagination
            pagination.id = PaginationUtils.prepareThreadKey(query)
            pagination.resourceType = Pagination.RESOURCE_TYPE_THREAD
            db.paginationDao().insert(pagination)
        }
    }

    fun deleteThreadFullInfo(thread: Thread) {
        deleteThreadById(thread.id)
        // There is no cached image to delete
        // deleteCachedImages(thread.getImages());
    }

    fun deleteThreadById(threadId: String?) {
        db.threadDao().deleteById(threadId)
    }

    private fun deleteThreadByClientId(clientId: String?) {
        db.threadDao().deleteByClientId(clientId)
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 18
        const val FILTER_TYPE_ALL = 0
        const val FILTER_TYPE_CROP = 1
        const val FILTER_TYPE_LIVESTOCK = 2
    }
}
