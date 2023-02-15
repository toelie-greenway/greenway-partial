package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordResult
import kotlinx.coroutines.flow.Flow

interface ProductionRecordRepository {
    suspend fun saveProductionRecord(request: SaveProductionRecordRequest): SaveProductionRecordResult
    fun getProductionsStream(
        seasonId: String
    ): Flow<Result<List<ProductionRecord>>>
}