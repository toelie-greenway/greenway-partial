package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.db.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultExpenseCategoryRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : ExpenseCategoryRepository {

    override fun getExpensesByCategoryStream(): Flow<List<CategoryExpense>> {
        return emptyFlow()
    }

    override fun getExpenseCategoriesStream(seasonId: String): Flow<List<ExpenseCategory>> {
        return flow {
            emit(
                network.getExpenseCategories(userHelper.activeUserId.toString())
                    .map(NetworkExpenseCategory::asDomainModel)
            )
        }
    }
}