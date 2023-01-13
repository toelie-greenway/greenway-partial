package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    fun observeClosedSeasons(): Flow<Result<List<Season>>>
}