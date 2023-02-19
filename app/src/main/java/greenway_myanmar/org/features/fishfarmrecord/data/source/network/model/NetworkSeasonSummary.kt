package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.google.android.gms.maps.model.LatLng
import greenway_myanmar.org.features.fishfarmrecord.data.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Area
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncomeSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmMeasurement
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonSummary
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable

@Serializable
data class NetworkSeasonSummary(
    val id: String,
    val season: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val is_end: Boolean? = null,
    val is_harvest: Boolean? = null,
    val area: NetworkFarmArea? = null,
    val end_reason: NetworkSeasonEndReason? = null,
    val farm_name: String? = null,
    val fish_types: List<NetworkSeasonFishType>? = null,
    val company: NetworkContractFarmingCompany? = null,
    val total_cost: Double? = null,
    val family_cost: Double? = null,
    val expenses: List<NetworkCategoryExpense>? = null,
    val productions: NetworkProductionRecordSummary? = null,
    val crop_incomes: List<NetworkCropIncome>? = null,
    val fcr_records: List<NetworkFcrRecord>? = null,
    //TODO: loan, productions, fcrs
    //val loan: NetworkLoan? = null,
)


fun NetworkSeasonSummary.asDomainModel() = SeasonSummary(
    id = id,
    seasonName = season.orEmpty(),
    farmName = farm_name.orEmpty(),
    startDate = start_date.toInstantOrNow(),
    endDate = end_date.toInstantOrNow(),
    farmMeasurement = FarmMeasurement(
        location = null, //TBC: mapLocationOrNull(lat, lng),
        coordinates = null, // TBC, coordinates,
        area = Area.acre(0.0), //TBC,
        measuredArea = null, //TBC: Area.acreOrNull(measuredArea),
        measuredType = null, //TBC:  AreaMeasureMethod.fromStringOrNull(measuredType),
        depth = null, //TBC: depth
    ),
    totalExpenses = total_cost.toBigDecimalOrZero(),
    fishes = fish_types.orEmpty().map {
        it.asDomainModel()
    },
    company = company?.asDomainModel(),
    loan = null, //TBC:,
    isHarvested = is_harvest ?: false,
    endReason = end_reason?.asDomainModel(),
    familyCost = family_cost.toBigDecimalOrZero(),
    expenseSummary = mapExpenseSummary(expenses),
    productionRecordSummary = productions?.asDomainModel(),
    cropIncomeSummary = mapCropIncomeSummary(crop_incomes),
    fcrRecords = fcr_records.orEmpty().map(NetworkFcrRecord::asDomainModel)
)

private fun NetworkSeasonSummary.mapCropIncomeSummary(cropIncomes: List<NetworkCropIncome>?): CropIncomeSummary? {
    if (cropIncomes == null) return null

    return CropIncomeSummary(
        totalIncome = cropIncomes.sumOf { it.income.toBigDecimalOrZero() },
        cropIncomes = cropIncomes.map(NetworkCropIncome::asDomainModel)
    )
}


private fun NetworkSeasonSummary.mapExpenseSummary(expenses: List<NetworkCategoryExpense>?): ExpenseSummary? {
    if (expenses == null) return null

    return ExpenseSummary(
        totalExpense = expenses.sumOf { it.total_cost.toBigDecimalOrZero() },
        categoryExpenses = expenses.map(NetworkCategoryExpense::asDomainModel)
    )
}

private fun mapLocationOrNull(lat: Double?, lng: Double?): LatLng? {
    if (lat == null || lng == null) return null

    return LatLng(lat, lng)
}