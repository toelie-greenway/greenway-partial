package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class DefaultExpenseCategoryRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource,
    private val userHelper: UserHelper
) : ExpenseCategoryRepository {

    private val cachedCategoriesMutex = Mutex()
    private var cachedCategories: List<ExpenseCategory> = emptyList()

    override fun getExpenseCategoriesStream(forceRefresh: Boolean): Flow<List<ExpenseCategory>> {
        return flow {
            if (forceRefresh || cachedCategories.isEmpty()) {
                val networkResult = network.getExpenseCategories(userHelper.activeUserId.toString())
                    .map(NetworkExpenseCategory::asDomainModel)
                cachedCategoriesMutex.withLock {
                    cachedCategories = networkResult
                }
            }
            emit(cachedCategoriesMutex.withLock { cachedCategories })
        }

    }
}