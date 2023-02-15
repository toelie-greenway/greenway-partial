package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ProductionRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductionsStreamUseCase @Inject constructor(
    private val repository: ProductionRecordRepository
) {
    operator fun invoke(request: GetProductionsRequest): Flow<Result<List<ProductionRecord>>> {
        return repository.getProductionsStream(request.seasonId)
    }

    data class GetProductionsRequest(
        val seasonId: String
    )
}