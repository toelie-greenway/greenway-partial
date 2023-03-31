/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenway_myanmar.org.repository.boundarycallback

import android.arch.paging.PagingRequestHelper
import androidx.lifecycle.LiveData
import androidx.paging.PagedList.BoundaryCallback
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.ui.threads.ThreadsViewModel
import greenway_myanmar.org.util.PaginationUtils
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Thread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 *
 *
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class ThreadManualBoundaryCallback(
    private val appExecutors: AppExecutors,
    private val db: GreenWayDb,
    private val webservice: GreenWayWebservice,
    private val networkPageSize: Int,
    private val dataResponseCallback: DataResponseCallback,
    userHelper: UserHelper,
    query: ThreadsViewModel.Query
) : BoundaryCallback<Thread>() {
    val pagingRequestHelper: PagingRequestHelper = PagingRequestHelper(appExecutors.diskIO())
    private val userHelper: UserHelper
    private val query: ThreadsViewModel.Query
    val networkState: LiveData<NetworkState>

    init {
        this.userHelper = userHelper
        this.query = query
        networkState = pagingRequestHelper.createStatusLiveData()
    }

    /** Database returned 0 items. We should query the backend for more items.  */
    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded")
        pagingRequestHelper.runIfNotRunning(
            PagingRequestHelper.RequestType.INITIAL
        ) { request: PagingRequestHelper.Request.Callback ->
            webservice
                .getThreads(
                    1,
                    networkPageSize,
                    userHelper.activeUserId,
                    query.toApiQueryMap()
                )
                .enqueue(WebserviceCallback(request))
        }
    }

    /** User reached to the end of the list.  */
    override fun onItemAtEndLoaded(itemAtEnd: Thread) {
        Timber.d("onItemAtEndLoaded: ${itemAtEnd.body}")
        // no-op
//        appExecutors
//            .diskIO()
//            .execute {
//                val current = db.paginationDao()
//                    .findLoadResult(
//                        PaginationUtils.prepareThreadKey(query),
//                        Pagination.RESOURCE_TYPE_THREAD
//                    )
//                if (current != null) {
//                    val nextPage = current.currentPage + 1
//                    if (nextPage <= current.lastPage) {
//                        pagingRequestHelper.runIfNotRunning(
//                            PagingRequestHelper.RequestType.AFTER
//                        ) { request: PagingRequestHelper.Request.Callback ->
//                            webservice
//                                .getThreads(
//                                    nextPage,
//                                    networkPageSize,
//                                    userHelper.activeUserId,
//                                    query.toApiQueryMap()
//                                )
//                                .enqueue(WebserviceCallback(request))
//                        }
//                    }
//                } else {
//                    onZeroItemsLoaded()
//                }
//            }
    }

    fun requestNextPage() {
        appExecutors.diskIO().execute {
            val current = db.paginationDao()
                .findLoadResult(
                    PaginationUtils.prepareThreadKey(query),
                    Pagination.RESOURCE_TYPE_THREAD
                )
            if (current != null) {
                val nextPage = current.currentPage + 1
                if (nextPage <= current.lastPage) {
                    pagingRequestHelper.runIfNotRunning(
                        PagingRequestHelper.RequestType.AFTER
                    ) { request: PagingRequestHelper.Request.Callback ->
                        webservice
                            .getThreads(
                                nextPage,
                                networkPageSize,
                                userHelper.activeUserId,
                                query.toApiQueryMap()
                            )
                            .enqueue(WebserviceCallback(request))
                    }
                }
            } else {
                onZeroItemsLoaded()
            }
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
        response: Response<ThreadListResponse>, request: PagingRequestHelper.Request.Callback
    ) {
        appExecutors
            .diskIO()
            .execute {
                dataResponseCallback.handleResponse(response.body())
                request.recordSuccess()
            }
    }

    interface DataResponseCallback {
        fun handleResponse(response: ThreadListResponse?)
    }

    internal inner class WebserviceCallback(private val requestCallback: PagingRequestHelper.Request.Callback) :
        Callback<ThreadListResponse> {
        override fun onResponse(
            call: Call<ThreadListResponse>,
            response: Response<ThreadListResponse>
        ) {
            insertItemsIntoDb(response, requestCallback)
        }

        override fun onFailure(call: Call<ThreadListResponse>, t: Throwable) {
            requestCallback.recordFailure(t)
        }
    }
}