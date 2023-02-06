package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.ViewCompat
import androidx.core.view.children
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFcr
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.UIUtils
import java.math.BigDecimal

class FcrRatioListInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _onFcrRatioInputChangeListener: FcrRatioInputItemView.OnFcrRatioInputChangeListener? =
        null

    private val _fishes = mutableListOf<UiFish>()
    private val _feedWeights = mutableMapOf<Int, String?>()
    private val _gainWeights = mutableMapOf<Int, String?>()
    private val _ratios = mutableMapOf<Int, BigDecimal?>()
    private val _errors = mutableMapOf<Int, FcrRatioInputErrorUiState?>()
    private val _viewIdByIndexMap = mutableMapOf<Int, Int>()

    private val rootView: ViewGroup
        get() = this

    init {
        orientation = VERTICAL
    }

    fun setFishes(fishes: List<UiFish>) {
        onFishesChanged(fishes)
    }

    fun setFeedWeights(weights: Map<Int, String?>) {
        onFeedWeightsChanged(weights)
    }

    fun setGainWeights(weights: Map<Int, String?>) {
        onGainWeightsChanged(weights)
    }

    fun setCalculatedRatios(ratios: Map<Int, BigDecimal?>) {
        onCalculatedRatiosChanged(ratios)
    }

    fun setErrors(errors: Map<Int, FcrRatioInputErrorUiState?>?) {
        onErrorChanged(errors)
    }

    fun setItems(newItems: List<UiFcr>) {
        notifyDataSetChanged(newItems)
    }

    private fun notifyDataSetChanged(newItems: List<UiFcr>) {
//        rootView.removeAllViews()
//        rootView.isVisible = newItems.isNotEmpty()
//        newItems.forEachIndexed { index, uiFish ->
//            if (index > 0) {
//                rootView.addView(space8dp())
//            }
//
//            val itemView = createFcrRatioInput(uiFish)
//            rootView.addView(itemView)
//        }
    }

    private fun space24dp() = Space(context).apply {
        layoutParams = LayoutParams(0, UIUtils.dpToPx(context, 24))
    }

    private fun onFishesChanged(fishes: List<UiFish>) {
        if (_fishes == fishes) {
            return
        }
        _fishes.clear()
        _fishes.addAll(fishes)
        rootView.removeAllViews()

        fishes.forEachIndexed { index, uiFish ->
            if (index > 0) {
                rootView.addView(space24dp())
            }

            val itemView = createFcrRatioInput(
                index = index,
                fish = uiFish,
                feedWeight = _feedWeights[index],
                gainWeight = _gainWeights[index]
            )
            val viewId = ViewCompat.generateViewId()
            itemView.id = viewId
            _viewIdByIndexMap[index] = viewId
            rootView.addView(itemView)
        }
    }

    private fun createFcrRatioInput(
        index: Int,
        fish: UiFish,
        feedWeight: String?,
        gainWeight: String?,
    ): FcrRatioInputItemView =
        FcrRatioInputItemView(context).apply {
            this.init(
                index = index,
                fish = fish,
                feedWeight = feedWeight,
                gainWeight = gainWeight,
                onInputChangeListener = _onFcrRatioInputChangeListener)
        }

    private fun onFeedWeightsChanged(weights: Map<Int, String?>) {
        if (_feedWeights == weights) {
            return
        }
        _feedWeights.clear()
        _feedWeights.putAll(weights)
        for ((index, weight) in weights) {
            val childViewId = _viewIdByIndexMap[index]
            val childView = rootView.children.find { it.id == childViewId }
            if (childView is FcrRatioInputItemView && childView.getFeedWeight() != weight) {
                childView.setFeedWeight(weight)
            }
        }
    }

    private fun onGainWeightsChanged(weights: Map<Int, String?>) {
        if (_gainWeights == weights) {
            return
        }
        _gainWeights.clear()
        _gainWeights.putAll(weights)
        for ((index, weight) in weights) {
            val childViewId = _viewIdByIndexMap[index]
            val childView = rootView.children.find { it.id == childViewId }
            if (childView is FcrRatioInputItemView && childView.getGainWeight() != weight) {
                childView.setGainWeight(weight)
            }
        }
    }

    private fun onCalculatedRatiosChanged(ratios: Map<Int, BigDecimal?>) {
        if (_ratios == ratios) {
            return
        }
        _ratios.clear()
        _ratios.putAll(ratios)
        for ((index, ratio) in ratios) {
            val childViewId = _viewIdByIndexMap[index]
            val childView = rootView.children.find { it.id == childViewId }
            if (childView is FcrRatioInputItemView && childView.getRatio() != ratio) {
                childView.setRatio(ratio)
            }
        }
    }

    private fun onErrorChanged(errors: Map<Int, FcrRatioInputErrorUiState?>?) {
        if (_errors == errors) {
            return
        }
        _errors.clear()
        _errors.putAll(errors.orEmpty())

        for ((index, childViewId) in _viewIdByIndexMap) {
            val childView = rootView.children.find { it.id == childViewId }
            if (childView is FcrRatioInputItemView) {
                childView.setErrors(_errors[index])
            }
        }
    }

    fun setOnInputChangeListener(listener: FcrRatioInputItemView.OnFcrRatioInputChangeListener) {
        _onFcrRatioInputChangeListener = listener
        rootView.children.forEach { child ->
            if (child is FcrRatioInputItemView) {
                child.setOnInputChangeListener(listener)
            }
        }
    }
}