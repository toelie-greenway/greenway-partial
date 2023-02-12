package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClosedSeasonsStreamUseCase @Inject constructor(
    private val repository: SeasonRepository
) {
    operator fun invoke(request: GetClosedSeasonsRequest) : Flow<Result<List<Season>>> {
        return repository.getClosedSeasonsStream(request.farmId)
    }

    data class GetClosedSeasonsRequest(
        val farmId: String
    )
}