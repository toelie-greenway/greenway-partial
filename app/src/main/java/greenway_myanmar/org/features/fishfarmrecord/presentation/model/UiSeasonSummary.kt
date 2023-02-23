package greenway_myanmar.org.features.fishfarmrecord.presentation.model

import android.content.Context
import androidx.annotation.ColorRes
import com.greenwaymyanmar.core.presentation.util.numberFormat
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncomeSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ExpenseSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ProductionRecordSummary
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.domain.model.SeasonSummary
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.extensions.orZero
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.math.BigDecimal

data class UiSeasonSummary(
    val id: String,
    val farmMeasurement: UiFarmMeasurement,
    val seasonName: String,
    val farmName: String,
    val startDate: Instant,
    val endDate: Instant,
    val endReason: UiSeasonEndReason?,
    val isHarvested: Boolean,
    val fishes: List<UiFish>,
    val company: UiContractFarmingCompany? = null,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,
    val familyCost: BigDecimal = BigDecimal.ZERO,
    val expenseSummary: UiExpenseSummary?,
    val productionRecordSummary: UiProductionRecordSummary?,
    val cropIncomeSummary: UiCropIncomeSummary?,
    val fcrRecords: List<UiFcrRecord>?,
    val totalIncome: BigDecimal,
    val totalProfit: BigDecimal,
    val isProfit: Boolean
) {
    val formattedStartDate: String = DateUtils.format(startDate.toJavaInstant(), "d MMMM yyyy")
    val formattedEndDate: String = DateUtils.format(endDate.toJavaInstant(), "d MMMM yyyy")
    fun formattedTotalProductionWeights(context: Context): String =
        context.resources.getString(
            R.string.formatted_viss,
            numberFormat.format(productionRecordSummary?.totalWeight.orZero())
        )

    fun formattedTotalProductionIncomes(context: Context): String =
        context.resources.getString(
            R.string.formatted_kyat,
            numberFormat.format(productionRecordSummary?.totalIncome.orZero())
        )

    fun formattedTotalCropIncomes(context: Context): String =
        context.resources.getString(
            R.string.formatted_kyat,
            numberFormat.format(cropIncomeSummary?.totalIncome.orZero())
        )

    fun formattedTotalExpenses(context: Context): String =
        context.resources.getString(R.string.formatted_kyat, numberFormat.format(totalExpenses))

    fun formattedTotalIncomes(context: Context): String =
        context.resources.getString(
            R.string.formatted_kyat,
            numberFormat.format(totalIncome)
        )

    fun formattedFamilyCost(context: Context): String =
        context.resources.getString(
            R.string.formatted_kyat,
            numberFormat.format(familyCost)
        )

    fun formattedTotalProfit(context: Context): String =
        context.resources.getString(R.string.formatted_kyat, numberFormat.format(totalProfit))

    @ColorRes
    val profitTextColorResId = if (isProfit) {
        R.color.color_primary
    } else {
        R.color.color_error
    }

    companion object {
        fun fromDomainModel(domainModel: SeasonSummary) = UiSeasonSummary(
            id = domainModel.id,
            farmMeasurement = UiFarmMeasurement.fromDomain(domainModel.farmMeasurement),
            seasonName = domainModel.seasonName,
            farmName = domainModel.farmName,
            startDate = domainModel.startDate,
            endDate = domainModel.endDate,
            endReason = mapEndReason(domainModel.endReason),
            isHarvested = domainModel.isHarvested,
            fishes = domainModel.fishes.map {
                UiFish.fromDomain(it)
            },
            company = mapCompany(domainModel.company),
            totalExpenses = domainModel.totalExpenses,
            familyCost = domainModel.familyCost,
            expenseSummary = mapExpenseSummary(domainModel.expenseSummary),
            productionRecordSummary = mapProductionRecordSummary(domainModel.productionRecordSummary),
            cropIncomeSummary = mapCropIncomeSummary(domainModel.cropIncomeSummary),
            fcrRecords = domainModel.fcrRecords.orEmpty().map {
                UiFcrRecord.fromDomainModel(it)
            },
            totalIncome = domainModel.totalIncomes,
            totalProfit = domainModel.totalProfit,
            isProfit = domainModel.isProfit
        )

        private fun mapCropIncomeSummary(summary: CropIncomeSummary?): UiCropIncomeSummary? {
            if (summary == null) return null

            return UiCropIncomeSummary.fromDomainModel(summary)
        }

        private fun mapExpenseSummary(summary: ExpenseSummary?): UiExpenseSummary? {
            if (summary == null) return null

            return UiExpenseSummary.fromDomainModel(summary)
        }

        private fun mapEndReason(reason: SeasonEndReason?): UiSeasonEndReason? {
            if (reason == null) return null

            return UiSeasonEndReason.fromDomainModel(reason)
        }

        private fun mapCompany(company: ContractFarmingCompany?): UiContractFarmingCompany? {
            if (company == null) return null

            return UiContractFarmingCompany.fromDomain(company)
        }

        private fun mapProductionRecordSummary(summary: ProductionRecordSummary?): UiProductionRecordSummary? {
            if (summary == null) return null

            return UiProductionRecordSummary.fromDomainModel(summary)
        }

    }
}