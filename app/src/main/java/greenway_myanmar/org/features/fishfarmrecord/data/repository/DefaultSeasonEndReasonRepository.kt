package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonEndReasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEndReasonEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonEndReasonRepository
import greenway_myanmar.org.util.RateLimiter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val KEY_REASONS = "reasons"

class DefaultSeasonEndReasonRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource,
    private val ffrSeasonEndReasonDao: FfrSeasonEndReasonDao
) : SeasonEndReasonRepository {

    private val rateLimiter = RateLimiter<String>(7, TimeUnit.DAYS)

    override fun getSeasonEndReasonsStream(forceRefresh: Boolean): Flow<Result<List<SeasonEndReason>>> {
        return networkBoundResult(
            query = {
                ffrSeasonEndReasonDao.getReasonsStream()
                    .map { list -> list.map(FfrSeasonEndReasonEntity::asDomainModel) }
            },
            fetch = {
                network.getSeasonEndReasons().map(NetworkSeasonEndReason::asEntity)
            },
            saveFetchResult = { items ->
                ffrSeasonEndReasonDao.upsertReasonsEntities(items)
            },
            onFetchFailed = {
                rateLimiter.reset(KEY_REASONS)
            },
            shouldFetch = { data ->
                shouldFetchReasons(data, forceRefresh)
            }
        )
    }

    private fun shouldFetchReasons(data: List<SeasonEndReason>?, forceRefresh: Boolean): Boolean {
        return forceRefresh || (data.isNullOrEmpty() && rateLimiter.shouldFetch(KEY_REASONS))
    }
}