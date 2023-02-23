package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.data.repository.util.networkBoundResult
import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.db.helper.UserHelper
import greenway_myanmar.org.features.fishfarmrecord.data.model.asEntity
import greenway_myanmar.org.features.fishfarmrecord.data.model.asExpenseEntities
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrCategoryExpenseDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrExpenseDao
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCategoryExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CategoryExpense
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.CategoryExpenseRepository
import greenway_myanmar.org.util.RateLimiter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val KEY_CATEGORY_EXPENSES = "category-expenses"
private const val KEY_CATEGORY_EXPENSE = "category-expense-%s-%s"

class DefaultCategoryExpenseRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource,
    private val categoryExpenseDao: FfrCategoryExpenseDao,
    private val expenseDao: FfrExpenseDao,
    private val userHelper: UserHelper
) : CategoryExpenseRepository {

    private val rateLimiter = RateLimiter<String>(5, TimeUnit.MINUTES)

    override suspend fun getCategoryExpensesStream(
        seasonId: String,
        forceRefresh: Boolean
    ): Flow<Result<List<CategoryExpense>>> {
        return networkBoundResult(
            query = {
                categoryExpenseDao.getExpensesBySeasonStream(seasonId).map { list ->
                    list.map(FfrCategoryExpenseEntity::asDomainModel)
                }
            },
            fetch = {
                network.getCategoryExpenses(userHelper.activeUserId.toString(), seasonId).map {
                    it.asEntity(seasonId)
                }
            },
            saveFetchResult = { result ->
                categoryExpenseDao.upsertCategoryExpenseEntities(result)
            },
            onFetchFailed = {
                rateLimiter.reset(KEY_CATEGORY_EXPENSES)
            },
            shouldFetch = { data ->
                true//shouldFetchCategoryExpenses(data, forceRefresh)
            }
        )
    }

    override fun getCategoryExpenseStream(
        seasonId: String,
        categoryId: String,
        forceRefresh: Boolean
    ): Flow<Result<CategoryExpense?>> {
        return networkBoundResult(
            query = {
                categoryExpenseDao.getCategoryExpenseStream(
                    categoryId = categoryId,
                    seasonId = seasonId
                ).map { entities ->
                    if (!entities.isNullOrEmpty()) {
                        val entity = entities.iterator().next()
                        val (categoryExpense, expense) = entity
                        categoryExpense.asDomainModel(expense)
                    } else {
                        null
                    }

                }
            },
            fetch = {
                network.getCategoryExpense(
                    userId = userHelper.activeUserId.toString(),
                    categoryId = categoryId,
                    seasonId = seasonId
                )
            },
            saveFetchResult = { result ->
                Timber.d("Result: $result")
                val entity: FfrCategoryExpenseEntity = result.asEntity(seasonId)
                val entities: List<FfrExpenseEntity> =
                    result.asExpenseEntities(seasonId, categoryId)
                categoryExpenseDao.upsertCategoryExpenseEntity(entity)
                expenseDao.upsertExpenseEntities(entities)
            },
            onFetchFailed = {
                rateLimiter.reset(buildCategoryExpenseRateLimiterKey(seasonId, categoryId))
            },
            shouldFetch = { data ->
                shouldFetchCategoryExpense(seasonId, categoryId, data, forceRefresh)
            }

        )
//        GlobalScope.launch {
//            val a = network.getCategoryExpenses(
//                userId = userHelper.activeUserId.toString(),
//                seasonId = seasonId
//            )
//        }
        Timber.d("SeasonId=$seasonId, CategoryId=$categoryId")
        return emptyFlow()
    }

    private fun shouldFetchCategoryExpenses(
        data: List<CategoryExpense>?,
        forceRefresh: Boolean
    ): Boolean {
        return forceRefresh || (data.isNullOrEmpty() && rateLimiter.shouldFetch(
            KEY_CATEGORY_EXPENSES
        ))
    }

    private fun shouldFetchCategoryExpense(
        seasonId: String,
        categoryId: String,
        data: CategoryExpense?,
        forceRefresh: Boolean
    ): Boolean {
        return forceRefresh || (data == null && rateLimiter.shouldFetch(
            buildCategoryExpenseRateLimiterKey(
                seasonId,
                categoryId
            )
        ))
    }

    private fun buildCategoryExpenseRateLimiterKey(seasonId: String, categoryId: String) =
        KEY_CATEGORY_EXPENSE.format(seasonId, categoryId)

}