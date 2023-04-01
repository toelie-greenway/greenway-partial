package greenway_myanmar.org.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

/**
 * K = Key
 * V = Value
 * D = DataSource
 */
abstract class BaseDataSourceManualFactory<RequestType, ResultType> :
    DataSource.Factory<Int, ResultType>() {
    val sourceLiveData: MutableLiveData<BasePageKeyedManualGreenWayDataSource<RequestType, ResultType>> =
        MutableLiveData()

    override fun create(): DataSource<Int, ResultType> {
        val source = createDataSource()
        sourceLiveData.postValue(source)
        return source
    }

    abstract fun createDataSource(): BasePageKeyedManualGreenWayDataSource<RequestType, ResultType>

    fun refresh() {
        sourceLiveData.value?.invalidate()
    }

    fun retry() {
        sourceLiveData.value?.pagingRequestHelper?.retryAllFailed()
    }

    fun requestNextPage() {
        sourceLiveData.value?.requestNextPage()
    }
}
