package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase
import greenway_myanmar.org.vo.Listing
import javax.inject.Inject

class DefaultFarmRepository @Inject constructor() : FarmRepository {
    override fun getFarmListing(): Listing<Farm> {
        throw NotImplementedError()
    }

    override suspend fun getSeasonList(farmId: String): GetSeasonListUseCase.GetSeasonListResult {
        throw NotImplementedError()
    }

}
