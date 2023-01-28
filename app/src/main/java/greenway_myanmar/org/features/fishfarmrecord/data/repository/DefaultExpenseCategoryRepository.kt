package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultExpenseCategoryRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource
) : ExpenseCategoryRepository {

    override fun getExpenseCategoriesWithTotalExpensesStream(): Flow<Result<List<ExpenseCategoryWithTotalExpenses>>> {
        return emptyFlow()
    }

    override fun getExpenseCategoriesStream(): Flow<List<ExpenseCategory>> {
        return flow {
            emit(
                network.getExpenseCategories()
                    .map(NetworkExpenseCategory::asDomainModel)
            )
        }
    }
}