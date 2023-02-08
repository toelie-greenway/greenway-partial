package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.children
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import greenway_myanmar.org.util.UIUtils
import java.math.BigDecimal

class ProductionListInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _onProductionInputChangeListener: OnProductionInputChangeListener? = null

    private val _fishes = mutableListOf<UiFish>()
    private val _sizes = mutableListOf<UiFishSize>()
    private val _weightsByFishAndSize = mutableMapOf<Pair<String, UiFishSize>, String?>()
    private val _pricesByFishAndSize = mutableMapOf<Pair<String, UiFishSize>, String?>()
    private val _subtotalWeightByFish = mutableMapOf<String, BigDecimal>()
    private val _subtotalPriceByFish = mutableMapOf<String, BigDecimal>()

    private val rootView: ViewGroup
        get() = this

    init {
        orientation = VERTICAL
    }

    fun setFishesAndSizes(fishes: List<UiFish>, sizes: List<UiFishSize>) {
        onFishesChanged(fishes, sizes)
    }

    fun setWeights(weights: Map<Pair<String, UiFishSize>, String?>) {
        onWeightsChanged(weights)
    }

    fun setPrices(prices: Map<Pair<String, UiFishSize>, String?>) {
        onPricesChanged(prices)
    }

    fun setSubtotalWeight(weights: Map<String, BigDecimal>) {
        onSubtotalWeightChanged(weights)
    }

    fun setSubtotalPrice(prices: Map<String, BigDecimal>) {
        onSubtotalPriceChanged(prices)
    }

    private fun space16dp() = Space(context).apply {
        layoutParams = LayoutParams(0, UIUtils.dpToPx(context, 16))
    }

    private fun onFishesChanged(fishes: List<UiFish>, sizes: List<UiFishSize>) {
        if (_fishes == fishes && _sizes == sizes) {
            return
        }
        _fishes.clear()
        _fishes.addAll(fishes)

        _sizes.clear()
        _sizes.addAll(sizes)
        rootView.removeAllViews()

        fishes.forEachIndexed { index, uiFish ->
            if (index > 0) {
                rootView.addView(space16dp())
            }

            val itemView = createProductionInput(
                fish = uiFish,
                sizes = sizes,
            )
            rootView.addView(itemView)
        }
    }

    private fun createProductionInput(
        fish: UiFish,
        sizes: List<UiFishSize>
    ): ProductionInputItemView =
        ProductionInputItemView(context).apply {
            this.init(
                fish = fish,
                sizes = sizes,
                onWeightChanged = { fishId: String, size: UiFishSize, weight: String? ->
                    _onProductionInputChangeListener?.onWeightChanged(fishId, size, weight)
                },
                onPriceChanged = { fishId: String, size: UiFishSize, weight: String? ->
                    _onProductionInputChangeListener?.onPriceChanged(fishId, size, weight)
                },
            )
        }

    private fun onWeightsChanged(weights: Map<Pair<String, UiFishSize>, String?>) {
        if (_weightsByFishAndSize == weights) {
            return
        }
        _weightsByFishAndSize.clear()
        _weightsByFishAndSize.putAll(weights)
        for ((fishAndSize, weight) in weights) {
            val (fishId, size) = fishAndSize
            rootView.findChildByFishId(fishId)?.setWeight(weight, size)
        }
    }

    private fun onPricesChanged(prices: Map<Pair<String, UiFishSize>, String?>) {
        if (_pricesByFishAndSize == prices) {
            return
        }
        _pricesByFishAndSize.clear()
        _pricesByFishAndSize.putAll(prices)
        for ((fishAndSize, price) in prices) {
            val (fishId, size) = fishAndSize
            rootView.findChildByFishId(fishId)?.setPrice(price, size)
        }
    }

    private fun onSubtotalWeightChanged(subtotalWeights: Map<String, BigDecimal>) {
        if (_subtotalWeightByFish == subtotalWeights) {
            return
        }
        _subtotalWeightByFish.clear()
        _subtotalWeightByFish.putAll(subtotalWeights)
        for ((fishId, subtotalWeight) in subtotalWeights) {
            rootView.findChildByFishId(fishId)?.setSubtotalWeight(subtotalWeight)
        }
    }

    private fun onSubtotalPriceChanged(subtotalPrices: Map<String, BigDecimal>) {
        if (_subtotalPriceByFish == subtotalPrices) {
            return
        }
        _subtotalPriceByFish.clear()
        _subtotalPriceByFish.putAll(subtotalPrices)
        for ((fishId, subtotalPrice) in subtotalPrices) {
            rootView.findChildByFishId(fishId)?.setSubtotalPrice(subtotalPrice)
        }
    }

    private fun ViewGroup.findChildByFishId(fishId: String): ProductionInputItemView? {
        this.children.forEach { child ->
            if (child is ProductionInputItemView && child.getFish()?.id == fishId) {
                return child
            }
        }
        return null
    }

    fun setOnProductionInputChangeListener(listener: OnProductionInputChangeListener) {
        _onProductionInputChangeListener = listener
    }

    interface OnProductionInputChangeListener {
        fun onWeightChanged(fishId: String, size: UiFishSize, weight: String?)
        fun onPriceChanged(fishId: String, size: UiFishSize, price: String?)
    }
}
