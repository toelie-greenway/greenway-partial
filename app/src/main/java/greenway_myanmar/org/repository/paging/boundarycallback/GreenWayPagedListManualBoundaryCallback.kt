package greenway_myanmar.org.repository.paging.boundarycallback

import android.arch.paging.PagingRequestHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import greenway_myanmar.org.AppExecutors
import timber.log.Timber

abstract class GreenWayPagedListManualBoundaryCallback<RequestType, ResponseType : Any>(
    private val appExecutors: AppExecutors,
    dataResponseCallback: DataResponseCallback<RequestType>
) :
    GreenWayPagedListBoundaryCallback<RequestType, ResponseType>(
        appExecutors,
        dataResponseCallback
    ) {

    private var _hasMore = MutableLiveData<Boolean>()
    val hasMore: LiveData<Boolean> = _hasMore

    fun requestNextPage() {
        appExecutors.diskIO().execute {
            val current = getPagination()
            if (current != null) {
                val nextPage = current.currentPage + 1
                if (nextPage <= current.lastPage) {
                    pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { request: PagingRequestHelper.Request.Callback ->
                        createNextPageCall(nextPage).enqueue(WebserviceCallback(request))
                    }
                }
            } else {
                onZeroItemsLoaded()
            }
        }
    }

    /** User reached to the end of the list.  */
    override fun onItemAtEndLoaded(itemAtEnd: ResponseType) {
        Timber.d("onItemAtEndLoaded: $itemAtEnd")
        appExecutors.diskIO().execute {
            val current = getPagination()
            if (current != null) {
                val nextPage = current.currentPage + 1
                Timber.d("onItemAtEndLoaded: HasMore: ${nextPage <= current.lastPage}")
                _hasMore.postValue(nextPage <= current.lastPage)
            }
        }
    }
}