package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.epoxycontroller

import android.view.View.OnClickListener
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState.CategoryItem
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState.CloseItem
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.OpeningSeasonCategoryListItemUiState.ProductionItem
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCategoryLargeViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCategorySmallViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonCloseSeasonViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonProductionLargeViewModel_
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.views.OpeningSeasonProductionSmallViewModel_

class OpeningSeasonEpoxyController constructor(
    val onAddExpenseClick: (categoryId: String) -> Unit,
    val onViewCategoryExpensesClick: (categoryId: String) -> Unit,
    val onAddProductionClick: () -> Unit,
    val onViewProductionsClick: () -> Unit,
    val onCloseSeasonClick: () -> Unit
) :
    EpoxyController() {

    private var _items: List<OpeningSeasonCategoryListItemUiState> = emptyList()

    override fun buildModels() {
        _items.forEach { item ->
            buildItem(item)
                .addTo(this)
        }
    }

    private fun buildItem(item: OpeningSeasonCategoryListItemUiState): EpoxyModel<*> = when (item) {
        is CategoryItem -> {
            buildCategoryItem(item)
        }
        is ProductionItem -> {
            buildProductionItem(item)
        }
        CloseItem -> {
            buildCloseItem()
        }
    }

    private fun buildCategoryItem(item: CategoryItem): EpoxyModel<*> = if (item.hasRecord) {
        OpeningSeasonCategoryLargeViewModel_()
            .id("category${item.categoryId}")
            .item(item)
            .onAddNewExpenseClickCallback(OnClickListener {
                onAddExpenseClick(item.categoryId)
            })
            .onViewCategoryExpensesClickCallback(OnClickListener {
                onViewCategoryExpensesClick(item.categoryId)
            })
    } else {
        OpeningSeasonCategorySmallViewModel_()
            .id("category${item.categoryId}")
            .item(item)
            .onAddNewExpenseClickCallback(OnClickListener {
                onAddExpenseClick(item.categoryId)
            })
    }

    private fun buildProductionItem(item: ProductionItem): EpoxyModel<*> = if (item.hasRecord) {
        OpeningSeasonProductionLargeViewModel_()
            .id("production")
            .item(item)
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
    }

    private fun buildCloseItem(): EpoxyModel<*> = OpeningSeasonCloseSeasonViewModel_().apply {
        id("close_season")
        onCloseSeasonClickCallback(OnClickListener {
            onCloseSeasonClick()
        })
    }

    fun setItems(items: List<OpeningSeasonCategoryListItemUiState>) {
        _items = items
        requestModelBuild()
    }

}
