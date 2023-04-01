package greenway_myanmar.org.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

/**
 * K = Key
 * V = Value
 * D = DataSource
 */
abstract class BaseDataSourceFactory<RequestType, ResultType, Source : BasePageKeyedGreenWayDataSource<RequestType, ResultType>> :
    DataSource.Factory<Int, ResultType>() {
    val sourceLiveData: MutableLiveData<Source> = MutableLiveData()

    override fun create(): DataSource<Int, ResultType> {
        val source = createDataSource()
        sourceLiveData.postValue(source)
        return source
    }

    abstract fun createDataSource(): Source

    fun refresh() {
        sourceLiveData.value?.invalidate()
    }

    fun retry() {
        sourceLiveData.value?.pagingRequestHelper?.retryAllFailed()
    }
}
