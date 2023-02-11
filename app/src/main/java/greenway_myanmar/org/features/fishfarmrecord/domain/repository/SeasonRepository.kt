package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase.SaveSeasonRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase.SaveSeasonResult
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    fun getClosedSeasonsStream(farmId: String): Flow<List<Season>>
    suspend fun saveSeason(request: SaveSeasonRequest): SaveSeasonResult
    suspend fun endSeason(reason: SeasonEndReason)
}