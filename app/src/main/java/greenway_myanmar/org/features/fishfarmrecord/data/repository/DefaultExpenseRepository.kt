package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase.SaveExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.SaveExpenseUseCase.SaveExpenseResult
import greenway_myanmar.org.util.extensions.orZero
import java.math.BigDecimal
import javax.inject.Inject

class DefaultExpenseRepository @Inject constructor(
    private val seasonDao: FfrSeasonDao,
    private val farmDao: FfrFarmDao,
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : ExpenseRepository {

    override suspend fun saveExpense(request: SaveExpenseRequest): SaveExpenseResult {
        val response = network.postExpense(
            userId = userHelper.activeUserId.toString(),
            request = NetworkExpenseRequest.fromDomainRequest(request)
        )
        val season = seasonDao.getSeasonById(request.seasonId)

        if (season != null) {
            val isHarvested = if (season.isHarvested) {
                true
            } else {
                request.expenseCategory.isHarvesting
            }
            seasonDao.upsertSeasonEntity(
                season.copy(
                    totalExpenses = season.totalExpenses + calculateTotal(
                        request
                    ),
                    isHarvested = isHarvested
                )
            )
        }
        return SaveExpenseResult(response.id)
    }

    private fun calculateTotal(request: SaveExpenseRequest): BigDecimal {
        return request.labourCost.orZero() +
                request.machineryCost.orZero() +
                request.inputs.orEmpty()
                    .sumOf {
                        it.totalCost
                    }
    }

}