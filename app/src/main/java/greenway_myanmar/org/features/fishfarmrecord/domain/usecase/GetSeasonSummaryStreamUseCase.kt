package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeasonSummaryStreamUseCase @Inject constructor(
    val repository: SeasonRepository
) {
    operator fun invoke(request: GetSeasonSummaryRequest): Flow<Result<SeasonSummary>> {
        return repository.getSeasonSummaryStream(
            farmId = request.farmId,
            seasonId = request.seasonId
        )
    }

    data class GetSeasonSummaryRequest(
        val farmId: String,
        val seasonId: String
    )
}