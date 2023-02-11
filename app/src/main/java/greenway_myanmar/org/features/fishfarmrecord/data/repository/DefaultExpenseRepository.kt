package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asExpenseByCategoryDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.request.NetworkExpenseRequest
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseByCategory
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
            seasonDao.upsertSeason(
                season.copy(
                    totalExpenses = season.totalExpenses + calculateTotal(
                        request
                    )
                )
            )
        }
        return SaveExpenseResult(response.id)
    }

    override suspend fun getExpensesByCategory(seasonId: String): List<ExpenseByCategory> {
        return network.getExpenseCategories(seasonId).map { it.asExpenseByCategoryDomainModel() }
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