package greenway_myanmar.org.features.fishfarmrecord.data.repository.fake

import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.common.result.asResult
import greenway_myanmar.org.di.ApplicationScope
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseCategoryWithTotalExpenses
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject
import kotlin.random.Random

private const val SERVICE_LATENCY_IN_MILLIS = 700L

class FakeExpenseCategoryRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope,
) : ExpenseCategoryRepository {

    private val SERVICE_DATA: LinkedHashMap<String, ExpenseCategory> = LinkedHashMap()

    private val observableCategories = MutableStateFlow<List<ExpenseCategory>>(emptyList())

    private var shouldReturnError = false

    init {
        externalScope.launch {
            addCategories(
                ExpenseCategory(
                    id = "1",
                    name = "ပြင်ဆင်ခြင်း"
                ),
                ExpenseCategory(
                    id = "2",
                    name = "သားပေါက်ထည့်သွင်းခြင်း 1"
                ),
                ExpenseCategory(
                    id = "2",
                    name = "သားပေါက်ထည့်သွင်းခြင်း 2"
                ),
                ExpenseCategory(
                    id = "3",
                    name = "သားပေါက်ထည့်သွင်းခြင်း 3"
                ),
                ExpenseCategory(
                    id = "4",
                    name = "သားပေါက်ထည့်သွင်းခြင်း4"
                ),
                ExpenseCategory(
                    id = "5",
                    name = "သားပေါက်ထည့်သွင်းခြင်း5"
                ),
                ExpenseCategory(
                    id = "6",
                    name = "သားပေါက်ထည့်သွင်းခြင်း6"
                ),
                ExpenseCategory(
                    id = "7",
                    name = "သားပေါက်ထည့်သွင်းခြင်း7"
                ),
                ExpenseCategory(
                    id = "8",
                    name = "သားပေါက်ထည့်သွင်းခြင်း8"
                ),
                ExpenseCategory(
                    id = "9",
                    name = "သားပေါက်ထည့်သွင်းခြင်း9"
                ),
                ExpenseCategory(
                    id = "10",
                    name = "သားပေါက်ထည့်သွင်းခြင်း10"
                ),
                ExpenseCategory(
                    id = "11",
                    name = "သားပေါက်ထည့်သွင်းခြင်း11"
                ),
                ExpenseCategory(
                    id = "12",
                    name = "သားပေါက်ထည့်သွင်းခြင်း12"
                ),
                ExpenseCategory(
                    id = "13",
                    name = "သားပေါက်ထည့်သွင်းခြင်း13"
                ),
                ExpenseCategory(
                    id = "14",
                    name = "သားပေါက်ထည့်သွင်းခြင်း14"
                ),
                ExpenseCategory(
                    id = "15",
                    name = "သားပေါက်ထည့်သွင်းခြင်း15"
                ),
                ExpenseCategory(
                    id = "16",
                    name = "သားပေါက်ထည့်သွင်းခြင်း16"
                ),
                ExpenseCategory(
                    id = "17",
                    name = "သားပေါက်ထည့်သွင်းခြင်း17"
                ),
                ExpenseCategory(
                    id = "18",
                    name = "သားပေါက်ထည့်သွင်းခြင်း18",
                )
            )
        }
        externalScope.launch {
            observableCategories.value = getCategories()
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    suspend fun refreshCategories() {
        observableCategories.value = getCategories()
    }

    suspend fun getCategories(): List<ExpenseCategory> {
        return withContext(ioDispatcher) {
            // Simulate network by delaying the execution.
            val records = SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            records
        }
    }

    override fun getExpenseCategoriesWithTotalExpensesStream(): Flow<Result<List<ExpenseCategoryWithTotalExpenses>>> {
        return observableCategories.asResult {
            it.map { category ->
                val hasExpenses = Random.nextBoolean()
                ExpenseCategoryWithTotalExpenses(
                    category,
                    if (hasExpenses) BigDecimal(Random.nextDouble()) else BigDecimal.ZERO,
                    if (hasExpenses) Instant.now() else null
                )
            }
        }
    }

    override fun getExpenseCategoriesStream(): Flow<List<ExpenseCategory>> {
        return observableCategories
    }

    private suspend fun addCategories(vararg categories: ExpenseCategory) {
        categories.forEach { category ->
            addCategory(category.id, category)
        }
        refreshCategories()
    }

    private suspend fun addCategory(id: String, newCategory: ExpenseCategory) {
        SERVICE_DATA[id] = newCategory
    }
}