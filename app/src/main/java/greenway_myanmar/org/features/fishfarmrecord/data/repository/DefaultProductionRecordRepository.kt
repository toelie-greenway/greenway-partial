package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ProductionRecordRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveProductionRecordUseCase.SaveProductionRecordResult
import javax.inject.Inject

class DefaultProductionRecordRepository @Inject constructor(

): ProductionRecordRepository {

    override suspend fun saveProductionRecord(request: SaveProductionRecordRequest): SaveProductionRecordResult {
        return SaveProductionRecordResult(id = "123")
    }
}