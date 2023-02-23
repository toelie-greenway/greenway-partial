package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrCropIncomeDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkCropIncomeRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CropIncomeRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveCropIncomeUseCase.SaveCropIncomeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultCropIncomeRepository @Inject constructor(
    private val cropIncomeDao: FfrCropIncomeDao,
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : CropIncomeRepository {

    override suspend fun saveCropIncome(request: SaveCropIncomeRequest): SaveCropIncomeResult {
        val response = network.postCropIncome(
            userId = userHelper.activeUserId.toString(),
            request = NetworkCropIncomeRequest.fromDomainRequest(request)
        )
        val entity = response.asEntity(request.seasonId)
        cropIncomeDao.upsertCropIncome(entity)

        return SaveCropIncomeResult(entity.id)
    }

    override fun getCropIncomesStream(seasonId: String): Flow<Result<List<CropIncome>>> {
        return networkBoundResult(
            query = {
                cropIncomeDao.getCropIncomesStream(seasonId).map { list ->
                    list.map { it.asDomainModel() }
                }
            },
            fetch = {
                network.getCropIncomes(
                    userId = userHelper.activeUserId.toString(),
                    seasonId = seasonId
                )
            },
            saveFetchResult = { response ->
                cropIncomeDao.upsertCropIncomes(
                    response.map { it.asEntity(seasonId) }
                )
            },
            onFetchFailed = {

            },
            shouldFetch = {
                true
            }
        )
    }
}