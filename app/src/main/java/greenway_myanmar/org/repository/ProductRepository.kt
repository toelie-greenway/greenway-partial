package greenway_myanmar.org.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.repository.market_place.product.ProductByTagDataSourceFactory
import greenway_myanmar.org.util.kotlin.AbsentLiveData
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import greenway_myanmar.org.vo.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor(
    private val appExecutors: AppExecutors,
    private val webservice: GreenWayWebservice,
    private val gson: Gson,
    private val userHelper: UserHelper
) {

    fun getProductsByTag(): Listing<Product> {
        val sourceFactory =
            ProductByTagDataSourceFactory(
                webservice,
                appExecutors,
                gson,
                PAGE_SIZE
            )
        val builder = LivePagedListBuilder(sourceFactory, PAGE_SIZE)
        val networkState =
            Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState }
        val itemCount = Transformations.switchMap(sourceFactory.sourceLiveData) { it.itemCount }
        val hasMore = Transformations.switchMap(sourceFactory.sourceLiveData) { it.hasMore }

        val refreshTrigger = MutableLiveData<Void>()
        val refreshState =
            Transformations.switchMap(refreshTrigger) { AbsentLiveData.create<NetworkState?>() }
        return Listing(
            builder.build(),
            itemCount,
            networkState,
            refreshState,
            hasMore,
            {
                sourceFactory.refresh()
            },
            {
                sourceFactory.retry()
            },
            {
                sourceFactory.requestNextPage()
            }
        )
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
