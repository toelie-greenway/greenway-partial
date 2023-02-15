package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CropIncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCropIncomesStreamUseCase @Inject constructor(
    private val repository: CropIncomeRepository
) {
    operator fun invoke(request: GetCropIncomesRequest): Flow<Result<List<CropIncome>>> {
        return repository.getCropIncomesStream(request.seasonId)
    }

    data class GetCropIncomesRequest(
        val seasonId: String
    )
}