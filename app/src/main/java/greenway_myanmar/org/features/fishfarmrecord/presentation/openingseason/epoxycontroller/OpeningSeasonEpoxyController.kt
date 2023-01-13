package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.epoxycontroller

import android.view.View.OnClickListener
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.Typed2EpoxyController
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonFcrListItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonProductionListItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCategoryLargeViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCategorySmallViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCloseSeasonView
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCloseSeasonViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonFcrRecordSmallViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonProductionLargeViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonProductionSmallViewModel_

class OpeningSeasonEpoxyController constructor(
    val onAddExpenseClick: () -> Unit,
    val onViewCategoryExpensesClick: () -> Unit,
    val onAddProductionClick: () -> Unit,
    val onViewProductionsClick: () -> Unit,
    val onAddFcrClick: () -> Unit,
    val onViewFcrsClick: () -> Unit,
    val onCloseSeasonClick: () -> Unit
) :
    EpoxyController() {

    private var _categories: List<OpeningSeasonCategoryListItemUiState> = emptyList()

    private var _production: OpeningSeasonProductionListItemUiState? = null
    private var _showProduction: Boolean = false

    private var _fcr: OpeningSeasonFcrListItemUiState? = null
    private var _showFcr: Boolean = false

    private var _showCloseSeason: Boolean = false

    override fun buildModels() {
        if (_categories.isNotEmpty()) {
            _categories.forEachIndexed { index, category ->
                if (category.hasRecord) {
                    OpeningSeasonCategoryLargeViewModel_()
                        .id("category$index")
                        .item(category)
                        .onAddNewExpenseClickCallback(OnClickListener {
                            onAddExpenseClick()
                        })
                        .onViewCategoryExpensesClickCallback(OnClickListener {
                            onViewCategoryExpensesClick()
                        })
                } else {
                    OpeningSeasonCategorySmallViewModel_()
                        .id("category$index")
                        .item(category)
                        .onAddNewExpenseClickCallback(OnClickListener {
                            onAddExpenseClick()
                        })
                }.addTo(this)
            }
        }

        if (_showProduction) {
            if (_production?.hasRecord == true) {
                OpeningSeasonProductionLargeViewModel_()
                    .id("production")
                    .item(_production)
                    .onAddNewProductionClickCallback(OnClickListener {
                        onAddProductionClick()
                    })
                    .onViewProductionsClickCallback(OnClickListener {
                        onViewProductionsClick()
                    })
            } else {
                OpeningSeasonProductionSmallViewModel_()
                    .id("production")
                    .onAddNewProductionClickCallback(OnClickListener {
                        onAddProductionClick()
                    })
            }.addTo(this)
        }

        if (_showFcr) {
            if (_fcr?.hasRecord == true) {
                OpeningSeasonProductionLargeViewModel_()
                    .id("fcr")
                    .item(_production)
                    .onAddNewProductionClickCallback(OnClickListener {
                        onAddFcrClick()
                    })
                    .onViewProductionsClickCallback(OnClickListener {
                        onViewFcrsClick()
                    })
            } else {
                OpeningSeasonFcrRecordSmallViewModel_()
                    .id("fcr")
                    .onAddNewFcrClickCallback(OnClickListener {
                        onAddFcrClick()
                    })
            }.addTo(this)
        }

        if (_showCloseSeason) {
            OpeningSeasonCloseSeasonViewModel_()
                .id("close_season")
                .onCloseSeasonClickCallback(OnClickListener {
                    onCloseSeasonClick()
                })
                .addTo(this)
        }
    }

    fun setCategories(categories: List<OpeningSeasonCategoryListItemUiState>) {
        _categories = categories
        requestModelBuild()
    }

    fun setProduction(production: OpeningSeasonProductionListItemUiState) {
        _production = production
        requestModelBuild()
    }

    fun setShowProduction(show: Boolean) {
        _showProduction = show
        requestModelBuild()
    }

    fun setFcr(fcr: OpeningSeasonFcrListItemUiState) {
        _fcr = fcr
        requestModelBuild()
    }

    fun setShowFcr(show: Boolean) {
        _showFcr = show
        requestModelBuild()
    }

    fun setShowCloseSeason(show: Boolean) {
        _showCloseSeason = show
        requestModelBuild()
    }
}