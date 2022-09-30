package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.google.gson.Gson
import com.greenwaymyanmar.api.ApiEmptyResponse
import com.greenwaymyanmar.api.ApiErrorResponse
import com.greenwaymyanmar.api.ApiResponse
import com.greenwaymyanmar.api.ApiSuccessResponse
import greenway_myanmar.org.AppExecutors
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.data.repositories.datasource.FarmDataSource
import greenway_myanmar.org.features.farmingrecord.qr.data.repositories.datasource.FarmDataSourceFactory
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase.GetSeasonListResult
import greenway_myanmar.org.util.CommonConstants
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.NetworkState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFarmRepository @Inject constructor(
    private val qrService: QrService,
    private val appExecutors: AppExecutors,
    private val gson: Gson,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userHelper: UserHelper
) : FarmRepository {

    override fun getFarmListing(): Listing<Farm> {
        val sourceFactory =
            FarmDataSourceFactory(
                qrService,
                appExecutors,
                gson,
                userHelper.activeUserId
            )
        val builder =
            LivePagedListBuilder(sourceFactory, CommonConstants.NetworkConfig.DEFAULT_PAGE_SIZE)
        val networkState =
            Transformations.switchMap(sourceFactory.sourceLiveData) { obj: FarmDataSource ->
                obj.networkState
            }
        val itemCount =
            Transformations.switchMap(sourceFactory.sourceLiveData) { obj: FarmDataSource ->
                obj.itemCount
            }

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Void>()
        val refreshState =
            Transformations.switchMap(refreshTrigger) { input: Void? ->
                refresh()
            }
        return Listing(
            builder.build(),
            itemCount,
            networkState,
            refreshState,
            {
                if (sourceFactory.sourceLiveData.value != null) {
                    sourceFactory.sourceLiveData.value!!.invalidate()
                }
            },
            {
                if (sourceFactory.sourceLiveData.value != null) {
                    sourceFactory.sourceLiveData.value!!.pagingRequestHelper.retryAllFailed()
                }
            }
        )
    }

    private fun refresh(): LiveData<NetworkState> {
        // TODO: Implement refresh
        val mockRefreshState = MutableLiveData<NetworkState>()
        mockRefreshState.value = NetworkState.LOADING
        return mockRefreshState
    }

    override suspend fun getSeasonList(farmId: String): GetSeasonListResult {
        return withContext(ioDispatcher) {
            try {
                val response =
                    qrService.getHarvestedSeasonList(farmId, 1, userHelper.activeUserId.toString())
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetSeasonListResult.Success(
                            data = apiResponse.body.data.map {
                                it.toDomain()
                            })
                    }
                    is ApiErrorResponse -> {
                        GetSeasonListResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        GetSeasonListResult.Success(emptyList())
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetSeasonListResult.Error(ResourceError.from(e))
            }
        }
    }
}