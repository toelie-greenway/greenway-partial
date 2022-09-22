package com.greenwaymyanmar.repository.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.greenwaymyanmar.utils.errorMessage
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.BuildConfig
import greenway_myanmar.org.api.Pagination
import greenway_myanmar.org.vo.NetworkState
import paging.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BasePageKeyedGreenWayDataSource<RequestType, ResultType>
protected constructor(appExecutors: AppExecutors, private val gson: Gson) :
  PageKeyedDataSource<Int, ResultType>() {
  val pagingRequestHelper: PagingRequestHelper = PagingRequestHelper(appExecutors.diskIO())
  var totalPage = 0
    private set
  val itemCount: MutableLiveData<Int> = MutableLiveData()

  /**
   * There is no sync on the state because paging will always call loadInitial first then wait for
   * it to return some success value before calling loadAfter.
   */
  val networkState: LiveData<NetworkState> = pagingRequestHelper.createStatusLiveData()

  val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()

  override fun loadInitial(
    params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int, ResultType>
  ) {
    itemCount.postValue(-1)
    pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      request: PagingRequestHelper.Request.Callback ->
      createCall()
        .enqueue(
          object : Callback<RequestType> {
            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
              if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val items = getItemsFromResponse(body)
                val pagination = getPaginationFromResponseBody(body)
                // keep total page and item count for later access
                totalPage = pagination.lastPage
                itemCount.value = pagination.total
                callback.onResult(items, pagination.currentPage - 1, pagination.currentPage + 1)
                initialLoad.postValue(NetworkState.LOADED)
                request.recordSuccess()
              } else {
                val message = response.errorMessage()
                initialLoad.postValue(NetworkState.error(message))
                request.recordFailure(Exception(message))
              }
            }

            override fun onFailure(call: Call<RequestType>, t: Throwable) {
              val errorMessage: String? =
                if (BuildConfig.DEBUG) {
                  t.message
                } else {
                  "ချိတ်ဆက်မှု မအောင်မြင်ပါ။ မိတ်ဆွေရဲ့ အင်တာနက်လိုင်းအား စစ်ဆေး၍ ပြန်လည်ကြိုးစားကြည့်ပါ။"
                }
              val error = NetworkState.error(errorMessage)
              initialLoad.postValue(error)
              request.recordFailure(t)
            }
          }
        )
    }
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ResultType>) {
    // ignored, since we only ever append to our initial load
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResultType>) {
    val nextPage = params.key
    if (nextPage <= totalPage) {
      pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
        request: PagingRequestHelper.Request.Callback ->
        createNextPageCall(nextPage)
          .enqueue(
            object : Callback<RequestType> {
              override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
                if (response.isSuccessful) {
                  val body = response.body()
                  if (body != null) {
                    val items = getItemsFromResponse(body)
                    val pagination = getPaginationFromResponseBody(body)
                    callback.onResult(items, pagination.currentPage + 1)
                    request.recordSuccess()
                  }
                }
              }

              override fun onFailure(call: Call<RequestType>, t: Throwable) {
                request.recordFailure(t)
              }
            }
          )
      }
    }
  }

  protected abstract fun createCall(): Call<RequestType>
  protected abstract fun createNextPageCall(nextPage: Int): Call<RequestType>
  protected abstract fun getItemsFromResponse(listResponse: RequestType): List<ResultType>
  protected abstract fun getPaginationFromResponseBody(listResponse: RequestType): Pagination

}
