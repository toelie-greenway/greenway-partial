package greenway_myanmar.org.features.farmingrecord.qr.domain.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase
import greenway_myanmar.org.vo.Listing

interface FarmRepository {
    fun getFarmListing(): Listing<Farm>
    suspend fun getSeasonList(farmId: String): GetSeasonListUseCase.GetSeasonListResult
}