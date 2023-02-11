package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import kotlinx.coroutines.flow.Flow

interface SeasonEndReasonRepository {
    fun getSeasonEndReasonsStream(forceRefresh: Boolean = false): Flow<Result<List<SeasonEndReason>>>
}
