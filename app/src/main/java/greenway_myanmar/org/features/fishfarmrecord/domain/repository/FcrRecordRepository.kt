package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordResult
import kotlinx.coroutines.flow.Flow

interface FcrRecordRepository {
    suspend fun saveFcrRecord(request: SaveFcrRecordRequest): SaveFcrRecordResult
    fun getFcrRecordsStream(): Flow<List<FcrRecord>>
}