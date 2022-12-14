package greenway_myanmar.org.features.fishfarmerrecordbook.data.repository.fake

import android.icu.text.AlphabeticIndex
import com.greenwaymyanmar.common.result.Result
import com.greenwaymyanmar.core.domain.model.Area
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Pond
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Season
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.repository.PondRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.concurrent.Task
import java.math.BigDecimal
import javax.inject.Inject

private const val SERVICE_LATENCY_IN_MILLIS = 2000L

class FakePondRepository @Inject constructor(

) : PondRepository {

    private val PONDS_SERVICE_DATA: LinkedHashMap<String, Pond> = LinkedHashMap()

    private val observablePonds = MutableStateFlow(runBlocking { getPonds() })

    private var shouldReturnError = false

    init {
        runBlocking {
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
            )
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    suspend fun refreshPonds() {
        observablePonds.value = getPonds()
    }

    suspend fun getPonds(): Result<List<Pond>> {
        // Simulate network by delaying the execution.
        val records = PONDS_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(records)
    }

    override fun getPondsStream(): Flow<Result<List<Pond>>> {
        return observablePonds
    }

    private suspend fun addPonds(vararg ponds: Pond) {
        ponds.forEach { pond ->
            addPond(pond.id, pond)
        }
    }

    private suspend fun addPond(id: String, newPond: Pond) {
        PONDS_SERVICE_DATA[id] = newPond
        refreshPonds()
    }
}