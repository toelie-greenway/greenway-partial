package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.group
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonSummary
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asString
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.SeasonSummaryItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.SeasonSummaryItemViewGroupModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.categoryExpenseListItemView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.cropIncomeListItemView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.productionRecordListItemView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummarySubheaderView

class SeasonSummaryController(
    private val context: Context
) : EpoxyController() {

    // private var farm: AsymtFarm? = null

    private var seasonSummary: UiSeasonSummary? = null

    override fun buildModels() {

        seasonSummary?.let { summary ->
            addSeasonSummary(summary)
            addProductionRecordSummaryIfExists(summary)
            addCropIncomeSummaryIfExists(summary)
            addExpenseSummaryIfExists(summary)
        }


//    seasonResource?.let { resource ->
//      resource.data?.let { _season ->


//
//          if (!_season.expenseCategories.isNullOrEmpty()) {
//
//            AsymtSeasonReportSubheaderBindingModel_()
//              .id("expense_list_subheader")
//              .title("ကုန်ကျစရိတ်များ")
//              .addTo(this)
//
//            _season.expenseCategories.orEmpty().forEach { expenseCategory ->
//              AsymtSeasonReportExpenseListItemBindingModel_()
//                .id("expense_category_${expenseCategory.id}")
//                .expenseCategory(expenseCategory)
//                .clickCallback(clickCallback)
//                .addTo(this)
//            }
//          }
//        }
//      }
//    }
    }

    private fun addSeasonSummary(summary: UiSeasonSummary) {
        SeasonSummaryItemViewGroupModel_()
            .id("items")
            .items(buildItemsFrom("TODO", summary))
            .addTo(this)
    }

    private fun addProductionRecordSummaryIfExists(summary: UiSeasonSummary) {
        summary.productionRecordSummary?.let { productionRecordSummary ->
            val records = productionRecordSummary.productionRecords
            if (records.isNotEmpty()) {
                group {
                    id("production_record_list")
                    layout(R.layout.ffr_season_summary_group_view)

                    seasonSummarySubheaderView {
                        id("production_list_subheader")
                        subheader("အထွက်နှုန်းစာရင်းများ")
                    }

                    records.forEach { item ->
                        productionRecordListItemView {
                            id("production_" + item.id)
                            item(item)
                        }
                    }
                }
            }
        }
    }


    private fun addCropIncomeSummaryIfExists(summary: UiSeasonSummary) {
        summary.cropIncomeSummary?.let { cropIncomeSummary ->
            val incomes = cropIncomeSummary.cropIncomes
            if (incomes.isNotEmpty()) {
                group {
                    id("crop_income_list")
                    layout(R.layout.ffr_season_summary_group_view)

                    seasonSummarySubheaderView {
                        id("crop_income_list_subheader")
                        subheader("သီးနှံဝင်ငွေများ")
                    }

                    incomes.forEach { item ->
                        cropIncomeListItemView {
                            id("crop_income_" + item.id)
                            item(item)
                        }
                    }
                }
            }
        }
    }

    private fun addExpenseSummaryIfExists(summary: UiSeasonSummary) {
        summary.expenseSummary?.let { expenseSummary ->
            val categoryExpenses = expenseSummary.categoryExpenses
            if (categoryExpenses.isNotEmpty()) {
                group {
                    id("expense_list")
                    layout(R.layout.ffr_season_summary_group_view)

                    seasonSummarySubheaderView {
                        id("expenses_list_subheader")
                        subheader("ကုန်ကျစရိတ်များ")
                    }

                    categoryExpenses.forEach { item ->
                        categoryExpenseListItemView {
                            id("expense_" + item.category.id)
                            item(item)
                        }
                    }
                }
            }
        }
    }

    interface ClickCallback {
        //fun onExpenseCategoryItemClick(expenseCategory: AsymtExpenseCategory?)
    }


    fun setSeasonSummary(summary: UiSeasonSummary?) {
        this.seasonSummary = summary
        requestModelBuild()
    }

    //
//  fun setFarm(farm: AsymtFarm) {
//    this.farm = farm
//    requestModelBuild()
//  }
//
    private fun buildItemsFrom(
        farm: String,
        season: UiSeasonSummary
    ): List<SeasonSummaryItemUiState> {
        val list = mutableListOf<SeasonSummaryItemUiState>()

        list.add(farmNameItem(farm))
        list.add(seasonNameItem(season.seasonName))
        list.add(fishesItem(season.fishes))
        list.add(seasonStartDateItem(season.formattedStartDate))
        list.add(seasonEndDateItem(season.formattedEndDate))
        list.add(seasonEndReasonItem(season.endReason))
        list.add(fishSpeciesItem(season.fishes))
        list.add(totalExpensesItem(season))
        list.add(totalProductionWeightsItem(season))
        list.add(totalProductionIncomesItem(season))
        list.add(totalCropIncomeItem(season))
        list.add(totalIncomeItem(season))
        list.add(totalProfitItem(season))
        list.add(formattedFamilyCost(season))
        return list
    }

    private fun formattedFamilyCost(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "family_cost",
            "မိသားစုလုပ်အားခ",
            season.formattedFamilyCost(context)
        )

    private fun totalProfitItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_profit",
            "စုစုပေါင်းအမြတ်ငွေ",
            season.formattedTotalProfit(context),
            season.profitTextColorResId
        )

    private fun totalIncomeItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_income",
            "စုစုပေါင်းဝင်ငွေ (ငါး + သီးနှံ)",
            season.formattedTotalIncomes(context)
        )

    private fun totalCropIncomeItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "crop_income",
            "သီးနှံဝင်ငွေ",
            season.formattedTotalCropIncomes(context)
        )

    private fun totalProductionWeightsItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_production_weight",
            "ငါးကန်အထွက်နှုန်း",
            season.formattedTotalProductionWeights(context)
        )

    private fun totalProductionIncomesItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_production_income",
            "ငါးကန်ဝင်ငွေ",
            season.formattedTotalProductionIncomes(context)
        )

    private fun totalExpensesItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_expenses",
            "ကုန်ကျငွေပေါင်း",
            season.formattedTotalExpenses(context)
        )

    private fun fishSpeciesItem(fishes: List<UiFish>) =
        SeasonSummaryItemUiState(
            "fish_species",
            "ငါးမျိုးစိတ်",
            fishes.filter { it.species.isNotEmpty() }
                .joinToString(separator = "၊ ") { it.species }
        )

    private fun seasonStartDateItem(date: String) =
        SeasonSummaryItemUiState(
            "season_start_date",
            "စတင်ရက်",
            date
        )

    private fun seasonEndDateItem(date: String) =
        SeasonSummaryItemUiState(
            "season_end_date",
            "ပိတ်သိမ်းရက်",
            date
        )


    private fun seasonEndReasonItem(endReason: UiSeasonEndReason?) =
        SeasonSummaryItemUiState(
            "end_reason",
            "ရာသီပိတ်သိမ်းခြင်းအခြေအနေ",
            endReason?.name.orEmpty()
        )

    private fun farmNameItem(farm: String) =
        SeasonSummaryItemUiState("farm_name", "လယ်ကွက်အမည်", farm)

    private fun seasonNameItem(seasonName: String) =
        SeasonSummaryItemUiState(
            "season_name",
            "မွေးမြူရာသီ",
            seasonName
        )

    private fun fishesItem(fishes: List<UiFish>) =
        SeasonSummaryItemUiState(
            "fishes",
            "မွေးမြူငါးများ",
            fishes.asString()
        )
}
