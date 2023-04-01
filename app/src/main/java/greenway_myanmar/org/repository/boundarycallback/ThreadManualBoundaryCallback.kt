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

import androidx.annotation.WorkerThread
import com.greenwaymyanmar.common.data.api.v1.Pagination
import com.greenwaymyanmar.common.data.api.v1.response.ThreadListResponse
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.GreenWayDb
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.ui.threads.ThreadsViewModel
import greenway_myanmar.org.util.PaginationUtils
import greenway_myanmar.org.vo.Thread
import retrofit2.Call

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 *
 *
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class ThreadManualBoundaryCallback(
    appExecutors: AppExecutors,
    private val db: GreenWayDb,
    private val webservice: GreenWayWebservice,
    private val networkPageSize: Int,
    dataResponseCallback: DataResponseCallback<ThreadListResponse>,
    private val userHelper: UserHelper,
    private val query: ThreadsViewModel.Query
) : GreenWayPagedListManualBoundaryCallback<ThreadListResponse, Thread>(
    appExecutors = appExecutors,
    dataResponseCallback = dataResponseCallback
) {

    override fun createCall(): Call<ThreadListResponse> {
       return webservice
            .getThreads(
                1,
                networkPageSize,
                userHelper.activeUserId,
                query.toApiQueryMap()
            )
    }

    override fun createNextPageCall(nextPage: Int): Call<ThreadListResponse> {
       return webservice
            .getThreads(
                nextPage,
                networkPageSize,
                userHelper.activeUserId,
                query.toApiQueryMap()
            )
    }

    @WorkerThread
    override fun getPagination(): Pagination? {
        return db.paginationDao()
            .findLoadResult(
                PaginationUtils.prepareThreadKey(query),
                Pagination.RESOURCE_TYPE_THREAD
            )
    }

}