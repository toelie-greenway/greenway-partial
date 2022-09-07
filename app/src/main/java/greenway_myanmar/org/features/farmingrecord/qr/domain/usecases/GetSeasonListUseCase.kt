package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Farm
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSeasonListUseCase @Inject constructor(
    private val farmRepository: FarmRepository
) {

    suspend operator fun invoke(params: Param): GetSeasonListResult {
        return farmRepository.getSeasonList(params.farmId)
    }

    data class Param(val farmId: String)

    sealed class GetSeasonListResult {
        data class Success(val data: List<Season>) : GetSeasonListResult()
        data class Error(val error: ResourceError) : GetSeasonListResult()
    }
}