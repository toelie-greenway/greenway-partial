package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonEndReasonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSeasonEndReasonsStreamUseCase @Inject constructor(
    private val repository: SeasonEndReasonRepository
) {
    operator fun invoke() : Flow<Result<List<SeasonEndReason>>> {
        return repository.getSeasonEndReasonsStream()
    }
}