package greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.controller

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.group
import greenway_myanmar.org.R
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonEndReason
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiSeasonSummary
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asString
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.SeasonSummaryItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.SeasonSummaryExpandToggleViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.SeasonSummaryItemViewGroupModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummaryCategoryExpenseCardView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummaryCropIncomeCardView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummaryFcrCardView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummaryProductionCardView
import greenway_myanmar.org.features.fishfarmrecord.presentation.season.summary.epoxy.views.seasonSummarySubheaderView

const val EXPAND_KEY_SUMMARY = "summary"
const val EXPAND_KEY_EXPENSE_CATEGORY = "category_%s"

class SeasonSummaryController(
    private val context: Context
) : EpoxyController() {

    private val expandedByKeys = mutableMapOf<String, Boolean>()
    private var seasonSummary: UiSeasonSummary? = null

    override fun buildModels() {

        seasonSummary?.let { summary ->
            addSeasonSummary(summary)
            addExpandShrinkButton()
            addProductionSummariesIfExists(summary)
            addCropIncomeSummaryIfExists(summary)
            addExpenseSummaryIfExists(summary)
            addFcrSummaryIfExists(summary)
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

    private fun addExpandShrinkButton() {
        SeasonSummaryExpandToggleViewModel_()
            .id("expand-button")
            .clickListener(View.OnClickListener {
                toggleExpanded(EXPAND_KEY_SUMMARY)
            })
            .expand(expandedByKeys.getOrDefault(EXPAND_KEY_SUMMARY, false))
            .addTo(this)
    }

    private fun addSeasonSummary(summary: UiSeasonSummary) {
        val expanded = expandedByKeys.getOrDefault(EXPAND_KEY_SUMMARY, false)
        SeasonSummaryItemViewGroupModel_()
            .id("items")
            .items(buildItemsFrom(summary.farmName, summary, expanded))
            .addTo(this)
    }

    private fun addProductionSummariesIfExists(summary: UiSeasonSummary) {
        if (summary.productionRecordSummary == null && summary.cropIncomeSummary == null) {
            return
        }
        group {
            id("production_summary_group")
            layout(R.layout.ffr_season_summary_group_view)

            seasonSummarySubheaderView {
                id("production_summary_group_subheader")
                subheader("ထုတ်လုပ်မှု မှတ်တမ်း")
            }

            summary.productionRecordSummary?.let { productionRecordSummary ->
                seasonSummaryProductionCardView {
                    id("production_summary")
                    item(productionRecordSummary)
                }
            }

            summary.cropIncomeSummary?.let { cropIncomeSummary ->
                seasonSummaryCropIncomeCardView {
                    id("crop_income_summary")
                    item(cropIncomeSummary)
                }
            }
        }
    }


    private fun addCropIncomeSummaryIfExists(summary: UiSeasonSummary) {
//        summary.cropIncomeSummary?.let { cropIncomeSummary ->
//            val incomes = cropIncomeSummary.cropIncomes
//            if (incomes.isNotEmpty()) {
//                group {
//                    id("crop_income_list")
//                    layout(R.layout.ffr_season_summary_group_view)
//
//                    seasonSummarySubheaderView {
//                        id("crop_income_list_subheader")
//                        subheader("သီးနှံဝင်ငွေများ")
//                    }
//
//                    incomes.forEach { item ->
//                        cropIncomeListItemView {
//                            id("crop_income_" + item.id)
//                            item(item)
//                        }
//                    }
//                }
//            }
//        }
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
                        seasonSummaryCategoryExpenseCardView {
                            id("expense_" + item.category.id)
                            expand(
                                expandedByKeys.getOrDefault(
                                    buildExpenseCategoryExpandKey(item.category.id),
                                    false
                                )
                            )
                            item(item)
                            clickListener(View.OnClickListener {
                                toggleExpanded(buildExpenseCategoryExpandKey(item.category.id))
                            })
                        }
                    }
                }
            }
        }
    }

    private fun addFcrSummaryIfExists(summary: UiSeasonSummary) {
        val fcrRecords = summary.fcrRecords
        if (!fcrRecords.isNullOrEmpty()) {
            group {
                id("expense_list")
                layout(R.layout.ffr_season_summary_group_view)

                seasonSummarySubheaderView {
                    id("fcr_record_list_subheader")
                    subheader("FCR စာရင်းများ")
                }

                seasonSummaryFcrCardView {
                    id("fcr_record_card")
                    items(fcrRecords)
                }
            }
        }
    }

    fun setSeasonSummary(summary: UiSeasonSummary?) {
        this.seasonSummary = summary
        requestModelBuild()
    }

    private fun toggleExpanded(key: String) {
        expandedByKeys[key] = !expandedByKeys.getOrDefault(key, false)
        requestModelBuild()
    }

    private fun buildItemsFrom(
        farm: String,
        season: UiSeasonSummary,
        expanded: Boolean
    ): List<SeasonSummaryItemUiState> {
        val list = mutableListOf<SeasonSummaryItemUiState>()

        list.add(farmNameItem(farm))
        list.add(seasonNameItem(season.seasonName))
        list.add(fishesItem(season.fishes))
        // list.add(seasonDateItem(season.formattedStartDate, season.formattedEndDate))
        list.add(seasonStartDateItem(season.formattedStartDate))

        if (expanded) {
            list.add(seasonEndDateItem(season.formattedEndDate))
            list.add(seasonEndReasonItem(season.endReason))
            if (season.fishes.any { it.species.isNotEmpty() }) {
                list.add(fishSpeciesItem(season.fishes))
            }
            list.add(totalProductionWeightsItem(season))
            list.add(totalExpensesItem(season))
            //list.add(totalProductionIncomesItem(season))
            // list.add(totalCropIncomeItem(season))
            list.add(totalIncomeItem(season))
            list.add(totalProfitItem(season))
            list.add(formattedFamilyCost(season))
        }
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
            "အမြတ်ငွေ",
            season.formattedTotalProfit(context),
            season.profitTextColorResId
        )

    private fun totalIncomeItem(season: UiSeasonSummary) =
        SeasonSummaryItemUiState(
            "total_income",
            "စုစုပေါင်းဝင်ငွေ (ကန် + သီးနှံ)",
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
            "စုစုပေါင်း ကုန်ထုတ်လုပ်မှု",
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
            "စုစုပေါင်း ကျန်ကျစားရိတ်",
            season.formattedTotalExpenses(context)
        )

    private fun fishSpeciesItem(fishes: List<UiFish>) =
        SeasonSummaryItemUiState(
            "fish_species",
            "ငါးမျိုးစိတ်",
            fishes.filter { it.species.isNotEmpty() }
                .joinToString(separator = "၊ ") { it.species }
        )

    private fun seasonDateItem(startDate: String, endDate: String) =
        SeasonSummaryItemUiState(
            "season_start_date",
            "နေ့စွဲ",
            "$startDate မှ $endDate"
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
            "ငါးအမျိုးအစား",
            fishes.asString()
        )

    private fun buildExpenseCategoryExpandKey(categoryId: String) =
        String.format(EXPAND_KEY_EXPENSE_CATEGORY, categoryId)
}
