package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FcrRecordRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveFcrRecordUseCase.SaveFcrRecordResult
import javax.inject.Inject

class DefaultFcrRecordRepository @Inject constructor(
    private val fcrRecordDao: FfrFcrRecordDao,
    private val fcrDao: FfrFcrDao
) : FcrRecordRepository {

    override suspend fun saveFcrRecord(request: SaveFcrRecordRequest): SaveFcrRecordResult {
        val pendingAction = if (request.id.isNullOrEmpty()) {
            PendingAction.CREATE
        } else {
            PendingAction.UPDATE
        }
        val rowId = fcrRecordDao.insertFcrRecord(
            entity = FfrFcrRecordEntity.from(request, pendingAction)
        )

        val fcrRecord = fcrRecordDao.getFcrRecordByRowId(rowId)
        val ratios = FfrFcrEntity.from(request, fcrRecord.id)
        fcrDao.upsertFcrEntities(ratios)

        return SaveFcrRecordResult(fcrRecord.id)
    }
}