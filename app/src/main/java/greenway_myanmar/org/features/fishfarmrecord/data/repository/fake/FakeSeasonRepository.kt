package greenway_myanmar.org.features.fishfarmrecord.data.repository.fake

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.di.ApplicationScope
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.SeasonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

private const val SERVICE_LATENCY_IN_MILLIS = 700L

class FakeSeasonRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope,
) : SeasonRepository {

    private val SEASONS_SERVICE_DATA: LinkedHashMap<String, Season> = LinkedHashMap()

    private val observableSeasons = MutableStateFlow<List<Season>>(emptyList())

    private var shouldReturnError = false

    init {
        externalScope.launch {
            addSeasons(
                Season(
                    id = "1",
                    name = "ပင်အိုအောက်",
                    totalExpenses = BigDecimal.TEN,
                    contractFarmingCompany = null,
                    startDate = LocalDateTime.of(1999, 7, 1, 0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant()
                ),
                Season(
                    id = "2",
                    name = "တဲနောက်",
                    totalExpenses = BigDecimal(4598.29282882),
                    contractFarmingCompany = null,
                    startDate = LocalDateTime.of(2007, 1, 24, 0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant()
                )
            )
        }

        externalScope.launch {
            observableSeasons.value = getSeasons()
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    suspend fun refreshSeasons() {
        observableSeasons.value = getSeasons()
    }

    suspend fun getSeasons(): List<Season> {
        return withContext(ioDispatcher) {
            // Simulate network by delaying the execution.
            val records = SEASONS_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            records
        }
    }

    private suspend fun addSeasons(vararg seasons: Season) {
        seasons.forEach { season ->
            addSeason(season.id, season)
        }
        refreshSeasons()
    }

    private suspend fun addSeason(id: String, newSeason: Season) {
        SEASONS_SERVICE_DATA[id] = newSeason
    }

    override fun observeClosedSeasons(): Flow<Result<List<Season>>> {
        return observableSeasons.map { Result.Success(it) }
    }
}