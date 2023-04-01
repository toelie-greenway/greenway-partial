package greenway_myanmar.org.repository.paging.datasource

import android.arch.paging.PagingRequestHelper
import com.google.gson.Gson
import greenway_myanmar.org.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

abstract class BasePageKeyedManualGreenWayDataSource<RequestType, ResultType> protected constructor(
    private val appExecutors: AppExecutors,
    private val gson: Gson
) : BasePageKeyedGreenWayDataSource<RequestType, ResultType>(
    appExecutors, gson
) {

    private var params: LoadParams<Int>? = null
    private var callback: LoadCallback<Int, ResultType>? = null

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ResultType>) {
        Timber.d("loadAfter: $currentPagination")
        this.params = params
        this.callback = callback

        updateHasMore()
    }

    fun requestNextPage() {
        val nextPage = params?.key ?: 0
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
                                            callback?.onResult(
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
}