package greenway_myanmar.org.repository.paging.datasource

import android.arch.paging.PagingRequestHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.errorText
import com.greenwaymyanmar.common.data.api.v1.Pagination
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.common.domain.entities.string
import greenway_myanmar.org.util.extensions.errorMessage
import greenway_myanmar.org.vo.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

abstract class BasePageKeyedGreenWayDataSource<RequestType, ResultType> protected constructor(
    appExecutors: AppExecutors,
    private val gson: Gson
) : PageKeyedDataSource<Int, ResultType>() {
    val pagingRequestHelper: PagingRequestHelper = PagingRequestHelper(appExecutors.diskIO())

    private val _itemCount: MutableLiveData<Int> = MutableLiveData()
    val itemCount: LiveData<Int> = _itemCount

    protected var currentPagination: Pagination? = null

    private val _hasMore: MutableLiveData<Boolean> = MutableLiveData()
    val hasMore: LiveData<Boolean> = _hasMore

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait for
     * it to return some success value before calling loadAfter.
     */
    val networkState: LiveData<NetworkState> = pagingRequestHelper.createStatusLiveData()
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()
    var totalPage = 0
        private set

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ResultType>
    ) {
        _itemCount.postValue(-1)
        pagingRequestHelper.runIfNotRunning(
            PagingRequestHelper.RequestType.INITIAL
        ) { request: PagingRequestHelper.Request.Callback ->
            createCall()
                .enqueue(
                    object : Callback<RequestType> {
                        override fun onResponse(
                            call: Call<RequestType>,
                            response: Response<RequestType>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                val items = getItemsFromResponse(body)
                                val pagination = getPaginationFromResponseBody(body)
                                if (pagination != null) {
                                    // keep total page and item count for later
                                    // access
                                    currentPagination = pagination
                                    totalPage = pagination.lastPage
                                    _itemCount.value = pagination.total
                                    updateHasMore()
                                    callback.onResult(
                                        items,
                                        pagination.currentPage - 1,
                                        pagination.currentPage + 1
                                    )
                                    initialLoad.postValue(NetworkState.LOADED)
                                    request.recordSuccess()
                                }
                            } else {
                                val message = response.errorMessage()
                                initialLoad.postValue(
                                    NetworkState.error(message)
                                )
                                request.recordFailure(Exception(message))
                            }
                        }

                        override fun onFailure(
                            call: Call<RequestType>, t: Throwable
                        ) {
                            val errorMessage = t.errorText().string()
                            val error = NetworkState.error(errorMessage)
                            initialLoad.postValue(error)
                            request.recordFailure(t)
                        }
                    })
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ResultType>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ResultType>
    ) {
        val nextPage = params.key
        if (nextPage <= totalPage) {
            pagingRequestHelper.runIfNotRunning(
                PagingRequestHelper.RequestType.AFTER
            ) { request: PagingRequestHelper.Request.Callback ->
                createNextPageCall(nextPage)
                    .enqueue(
                        object : Callback<RequestType> {
                            override fun onResponse(
                                call: Call<RequestType>,
                                response: Response<RequestType>
                            ) {
                                if (response.isSuccessful) {
                                    val body = response.body()
                                    if (body != null) {
                                        val items = getItemsFromResponse(body)
                                        val pagination = getPaginationFromResponseBody(body)
                                        if (pagination != null) {
                                            currentPagination = pagination
                                            updateHasMore()
                                            callback.onResult(
                                                items,
                                                pagination.currentPage + 1
                                            )
                                            request.recordSuccess()
                                        }
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<RequestType>, t: Throwable
                            ) {
                                request.recordFailure(t)
                            }
                        })
            }
        }
    }

    protected abstract fun createCall(): Call<RequestType>
    protected abstract fun createNextPageCall(nextPage: Int): Call<RequestType>
    protected abstract fun getItemsFromResponse(requestType: RequestType?): List<ResultType>
    protected abstract fun getPaginationFromResponseBody(requestType: RequestType?): Pagination?


    protected fun updateHasMore() {
        if (currentPagination != null) {
            val currentPage = currentPagination?.currentPage ?: 0
            val lastPage = currentPagination?.lastPage ?: 0
            val nextPage = currentPage + 1
            Timber.d("loadAfter: HasMore: ${nextPage <= lastPage}")
            _hasMore.postValue(nextPage <= lastPage)
        }
    }
}
