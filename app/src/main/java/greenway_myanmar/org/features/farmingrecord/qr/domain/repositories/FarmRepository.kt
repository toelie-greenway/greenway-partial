package greenway_myanmar.org.features.farmingrecord.qr.domain.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListUseCase
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase

interface FarmRepository {
    suspend fun getFarmList(): GetFarmListUseCase.GetFarmListResult
    suspend fun getSeasonList(farmId: String): GetSeasonListUseCase.GetSeasonListResult
}