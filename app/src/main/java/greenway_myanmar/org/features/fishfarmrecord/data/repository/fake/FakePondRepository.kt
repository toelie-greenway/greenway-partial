package greenway_myanmar.org.features.fishfarmrecord.data.repository.fake

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.di.ApplicationScope
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Farm
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.PondRepository
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase.CreateNewPondResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

private const val SERVICE_LATENCY_IN_MILLIS = 700L

class FakePondRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope,
) : PondRepository {

    private val PONDS_SERVICE_DATA: LinkedHashMap<String, Farm> = LinkedHashMap()

    private val observablePonds = MutableStateFlow<List<Farm>>(emptyList())

    private var shouldReturnError = false

    init {
        externalScope.launch {
            addPonds(
                Farm(
                    id = "1",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
//                    listOf(
//                        "https://picsum.photos/id/1/400/400",
//                        "https://picsum.photos/id/1/124/124"
//                    ),
                ),
                Farm(
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
                        ),
                        startDate = LocalDateTime.of(2021, 11, 3, 0, 0, 0)
                            .atZone(ZoneId.systemDefault()).toInstant()
                    )
                ),
                Farm(
                    id = "3",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "4",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "5",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "6",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "7",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "8",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "9",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "10",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "11",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "12",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "13",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "14",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "15",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "16",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "17",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "18",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "19",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "20",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "21",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "22",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "23",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "24",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "25",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
                    id = "26",
                    name = "ကန်တစ်",
                    area = Area.Acre(BigDecimal(2.0)),
                    images = emptyList(),
                ),
                Farm(
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

    suspend fun getPonds(): List<Farm> {
        return withContext(ioDispatcher) {
            // Simulate network by delaying the execution.
            val records = PONDS_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            records
        }
    }

    override fun observePonds(): Flow<Result<List<Farm>>> {
        return observablePonds.map { Result.Success(it) }
    }

    override fun observePond(pondId: String): Flow<Result<Farm>> {
        return flow {
            emit(PONDS_SERVICE_DATA.get(pondId)?.let {
                Result.Success(it)
            } ?: kotlin.run {
                Result.Error(Exception("Arr"))
            })
        }
    }

    private suspend fun addPonds(vararg farms: Farm) {
        farms.forEach { pond ->
            addPond(pond.id, pond)
        }
        refreshPonds()
    }

    private suspend fun addPond(id: String, newFarm: Farm) {
        PONDS_SERVICE_DATA[id] = newFarm
    }

    override suspend fun createPond(pondName: String): Result<CreateNewPondResult> {
        val newPondId = UUID.randomUUID().toString()
        addPonds(
            Farm(
                newPondId, pondName, emptyList(), null, Area.Acre(
                    BigDecimal(1.2)
                )
            )
        )
        return Result.Success(CreateNewPondResult(newPondId))
    }
}