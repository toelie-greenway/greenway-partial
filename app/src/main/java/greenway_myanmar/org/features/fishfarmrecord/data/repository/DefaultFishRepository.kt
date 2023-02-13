package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFishDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFishEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFish
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import greenway_myanmar.org.util.RateLimiter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val KEY_FISHES = "fishes"

class DefaultFishRepository @Inject constructor(
    private val fishDao: FfrFishDao,
    private val network: FishFarmRecordNetworkDataSource
) : FishRepository {

    private val rateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    override fun getFishesStream(
        query: String,
        forceRefresh: Boolean
    ): Flow<Result<List<Fish>>> {
        return networkBoundResult(
            query = { fishDao.getFishesStream(query).map { it.map(FfrFishEntity::asDomainModel) } },
            fetch = { network.getFishes() },
            saveFetchResult = { fishDao.upsertFishEntities(it.map(NetworkFish::asEntity)) },
            onFetchFailed = { rateLimiter.reset(KEY_FISHES) },
            shouldFetch = { data ->
                forceRefresh || (data.isNullOrEmpty() && rateLimiter.shouldFetch(
                    KEY_FISHES
                ))
            }
        )
    }
}