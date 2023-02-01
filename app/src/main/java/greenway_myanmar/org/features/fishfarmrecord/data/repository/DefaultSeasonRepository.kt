package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveSeasonUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultSeasonRepository @Inject constructor(
    private val seasonDao: FfrSeasonDao
) : SeasonRepository {

    override fun getClosedSeasonsStream(farmId: String): Flow<List<Season>> {
        return seasonDao.getSeasonsStream().map {
            it.map(FfrSeasonEntity::asDomainModel)
        }
    }

    override suspend fun saveSeason(request: SaveSeasonUseCase.SaveSeasonRequest): SaveSeasonUseCase.SaveSeasonResult {
        val action = if (request.id.isNullOrEmpty()) {
            PendingAction.CREATE
        } else {
            PendingAction.UPDATE
        }
        val entity = FfrSeasonEntity.from(request, action)
        seasonDao.upsertSeason(entity)
        return SaveSeasonUseCase.SaveSeasonResult(entity.id)
    }

}