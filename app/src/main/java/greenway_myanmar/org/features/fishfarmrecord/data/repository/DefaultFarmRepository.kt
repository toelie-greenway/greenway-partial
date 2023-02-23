package greenway_myanmar.org.features.fishfarmrecord.data.repository

import android.content.Context
import com.greenwaymyanmar.common.data.api.util.buildImageRequest
import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.vo.PendingAction
import dagger.hilt.android.qualifiers.ApplicationContext
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asNetworkRequestModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFarmUseCase.SaveFarmRequest
import greenway_myanmar.org.util.RateLimiter
import greenway_myanmar.org.vo.ServerImageContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val KEY_FARM_DETAIL = "farm_%s"

class DefaultFarmRepository @Inject constructor(
    private val farmDao: FfrFarmDao,
    private val seasonDao: FfrSeasonDao,
    private val userHelper: UserHelper,
    private val network: FishFarmRecordNetworkDataSource,
    @ApplicationContext private val context: Context
) : FarmRepository {

    private val rateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    override fun getFarmsStream(): Flow<Result<List<Farm>>> {
        // TODO: Replace with paging
        return networkBoundResult(
            query = {
                farmDao.getFarmsStream().map { list ->
                    list.map { farm ->
                        farm.asDomainModel()
                    }
                }
            },
            fetch = {
                network.getFarms(userHelper.activeUserId.toString())
            },
            saveFetchResult = { response ->
                response.data?.map { networkFarm ->
                    if (networkFarm.opening_season != null) {
                        seasonDao.upsertSeasonEntity(networkFarm.opening_season.asEntity(networkFarm.id))
                    }
                }
                farmDao.upsertFarms(response.data.orEmpty().map(NetworkFarm::asEntity))
            },
            onFetchFailed = {
                /* no-op */
            },
            shouldFetch = {
                true
            }
        )
    }

    override suspend fun saveFarm(request: SaveFarmRequest): SaveFarmUseCase.SaveFarmResult {
        val action = if (request.id.isNullOrEmpty()) {
            PendingAction.CREATE
        } else {
            PendingAction.UPDATE
        }
        val entity = FfrFarmEntity.from(request, action)
        farmDao.upsertFarm(entity)
        return SaveFarmUseCase.SaveFarmResult(entity.id)
    }

    override suspend fun postFarm(farmId: String): String {
        val farm: FfrFarmEntity = farmDao.getFarmById(farmId)

        val imageUrls = mutableListOf<String>()
        if (!farm.images.isNullOrEmpty()) {
            farm.images.map { it.uri }.forEach { imageUri ->
                if (imageUri != null) {
                    val networkImage = network.postImage(
                        buildImageRequest(
                            context.contentResolver,
                            ServerImageContext.FFR,
                            imageUri
                        )
                    )
                    imageUrls.add(networkImage.url.orEmpty())
                }
            }
        }
        val response = network.postFarm(
            userId = userHelper.activeUserId.toString(),
            request = farm.asNetworkRequestModel(imageUrls)
        )
        farmDao.deleteFarmById(farmId)
        farmDao.upsertFarm(response.asEntity())
        return response.id
    }

    override fun getFarmStream(farmId: String, forceRefresh: Boolean): Flow<Result<Farm?>> {
        return networkBoundResult(
            query = { farmDao.getFarmStreamById(farmId).map { it?.asDomainModel() } },
            fetch = { network.getFarm(farmId, userHelper.activeUserId.toString()) },
            saveFetchResult = { response -> farmDao.upsertFarm(response.asEntity()) },
            onFetchFailed = { rateLimiter.reset(buildFarmRateLimiterKey(farmId)) },
            shouldFetch = { data -> shouldFetchFarm(data, farmId, forceRefresh) }
        )
    }

    private fun shouldFetchFarm(data: Farm?, farmId: String, forceRefresh: Boolean): Boolean {
        return forceRefresh || (data == null && rateLimiter.shouldFetch(
            buildFarmRateLimiterKey(farmId)
        ))
    }

    private fun buildFarmRateLimiterKey(farmId: String) =
        KEY_FARM_DETAIL.format(farmId)

    override fun getFarmMeasurementStream(farmId: String): Flow<FarmMeasurement?> {
        return farmDao.getFarmStreamById(farmId).map {
            val farm = it?.farm
            if (farm != null) {
                FarmMeasurement(
                    location = farm.location,
                    coordinates = farm.coordinates,
                    area = Area.acre(farm.area),
                    measuredArea = if (farm.measuredArea != null) Area.acre(farm.measuredArea) else null,
                    measuredType = AreaMeasureMethod.fromStringOrNull(farm.measuredType),
                    depth = farm.depth
                )
            } else {
                null
            }
        }
    }

    override fun observePond(pondId: String): Flow<Result<Farm>> {
        return flow {
            // TODO: Implement
        }
    }
}