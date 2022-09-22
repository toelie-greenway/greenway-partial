package greenway_myanmar.org.features.farmingrecord.qr.data.repositories.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.gson.Gson
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm

class FarmDataSourceFactory(
    private val webservice: QrService,
    private val appExecutors: AppExecutors,
    private val gson: Gson,
    private val userId: Int
) : DataSource.Factory<Int, Farm>() {

    val sourceLiveData: MutableLiveData<FarmDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, Farm> {
        val source = FarmDataSource(webservice, appExecutors, gson, userId)
        sourceLiveData.postValue(source)
        return source.map {
            it.toDomain()
        }
    }
}
