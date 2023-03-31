package greenway_myanmar.org.repository.boundarycallback

import android.arch.paging.PagingRequestHelper
import greenway_myanmar.org.AppExecutors

abstract class GreenWayPagedListManualBoundaryCallback<RequestType, ResponseType : Any>(
  private val appExecutors: AppExecutors,
  dataResponseCallback: DataResponseCallback<RequestType>
) :
  GreenWayPagedListBoundaryCallback<RequestType, ResponseType>(appExecutors, dataResponseCallback) {

  init {

  }

  fun requestNextPage() {
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
      }
    }
  }

  override fun onItemAtEndLoaded(itemAtEnd: ResponseType) {
    // Do nothing, as we want to load the next page manually
  }
}
