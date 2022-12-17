package greenway_myanmar.org.features.fishfarmerrecordbook.data.repository.fake

import android.icu.text.AlphabeticIndex
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.model.Area
import greenway_myanmar.org.di.ApplicationScope
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Pond
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Season
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.repository.PondRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.internal.concurrent.Task
import java.math.BigDecimal
import javax.inject.Inject

private const val SERVICE_LATENCY_IN_MILLIS = 700L

class FakePondRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope,
) : PondRepository {

    private val PONDS_SERVICE_DATA: LinkedHashMap<String, Pond> = LinkedHashMap()

    private val observablePonds = MutableStateFlow<List<Pond>>(emptyList())

    private var shouldReturnError = false

    init {
        externalScope.launch {
            addPonds(
                Pond(
                    id = "1",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
//                    listOf(
//                        "https://picsum.photos/id/1/400/400",
//                        "https://picsum.photos/id/1/124/124"
//                    ),
                ),
                Pond(
                    id = "2",
                    name = "အမွှာကန်",
                    images = listOf(
                        "https://picsum.photos/id/3/690/960",
                        "https://picsum.photos/id/4/960/690"
                    ),
                    area = Area.Acre(BigDecimal(45.3689754)),
                    ongoingSeason = Season(
                        id = "1",
                        name = "ဆောင်းရာသီ",
                        totalExpenses = BigDecimal(234009),
                        contractFarmingCompany = ContractFarmingCompany(
                            id = "1",
                            name = "အစိမ်းရောင်လမ်း"
                        )
                    )
                ),
                Pond(
                    id = "3",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "4",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "5",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "6",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "7",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "8",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "9",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "10",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "11",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "12",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "13",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "14",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "15",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "16",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "17",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "18",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "19",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "20",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "21",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "22",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "23",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "24",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "25",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "26",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Pond(
                    id = "27",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                )
            )
        }

        externalScope.launch {
            observablePonds.value = getPonds()
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    suspend fun refreshPonds() {
        observablePonds.value = getPonds()
    }

    suspend fun getPonds(): List<Pond> {
        return withContext(ioDispatcher) {
            // Simulate network by delaying the execution.
            val records = PONDS_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            records
        }
    }

    override fun observePonds(): Flow<Result<List<Pond>>> {
        return observablePonds.map { Result.Success(it) }
    }

    private suspend fun addPonds(vararg ponds: Pond) {
        ponds.forEach { pond ->
            addPond(pond.id, pond)
        }
        refreshPonds()
    }

    private suspend fun addPond(id: String, newPond: Pond) {
        PONDS_SERVICE_DATA[id] = newPond
    }
}