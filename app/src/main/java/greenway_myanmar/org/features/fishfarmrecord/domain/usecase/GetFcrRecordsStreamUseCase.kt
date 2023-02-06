package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FcrRecord
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FcrRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFcrRecordsStreamUseCase @Inject constructor(
    private val fcrRecordRepository: FcrRecordRepository
) {
    operator fun invoke(): Flow<List<FcrRecord>> {
        return fcrRecordRepository.getFcrRecordsStream()
    }
}