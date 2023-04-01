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
package greenway_myanmar.org.repository.paging.boundarycallback

import android.arch.paging.PagingRequestHelper
import androidx.lifecycle.LiveData
import androidx.paging.PagedList.BoundaryCallback
import com.greenwaymyanmar.common.data.api.v1.Pagination
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.vo.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 *
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
abstract class GreenWayPagedListBoundaryCallback<RequestType, ResponseType : Any>(
  private val appExecutors: AppExecutors,
  private val dataResponseCallback: DataResponseCallback<RequestType>
) : BoundaryCallback<ResponseType>() {

  val pagingRequestHelper: PagingRequestHelper = PagingRequestHelper(appExecutors.diskIO())
  val networkState: LiveData<NetworkState> = pagingRequestHelper.createStatusLiveData()

  /** Database returned 0 items. We should query the backend for more items. */
  override fun onZeroItemsLoaded() {
    pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      request: PagingRequestHelper.Request.Callback ->
      createCall().enqueue(WebserviceCallback(request))
    }
  }

  /** User reached to the end of the list. */
  override fun onItemAtEndLoaded(itemAtEnd: ResponseType) {
    appExecutors.diskIO().execute {
      val current = getPagination()
      if (current != null) {
        val nextPage = current.currentPage + 1
        if (nextPage <= current.lastPage) {
          pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            request: PagingRequestHelper.Request.Callback ->
            createNextPageCall(nextPage).enqueue(WebserviceCallback(request))
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
    response: Response<RequestType>,
    request: PagingRequestHelper.Request.Callback
  ) {
    appExecutors.diskIO().execute {
      dataResponseCallback.handleResponse(response.body())
      request.recordSuccess()
    }
  }

  protected abstract fun createNextPageCall(nextPage: Int): Call<RequestType>
  protected abstract fun createCall(): Call<RequestType>
  protected abstract fun getPagination(): Pagination?

  interface DataResponseCallback<RequestType> {
    fun handleResponse(response: RequestType?)
  }

  internal inner class WebserviceCallback(
    private val requestCallback: PagingRequestHelper.Request.Callback
  ) : Callback<RequestType> {
    override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
      insertItemsIntoDb(response, requestCallback)
    }

    override fun onFailure(call: Call<RequestType>, t: Throwable) {
      requestCallback.recordFailure(t)
    }
  }
}
