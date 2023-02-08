package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrProductionInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.util.kotlin.customViewBinding
import java.math.BigDecimal
import java.text.NumberFormat

private val numberFormat: NumberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())

class ProductionInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _fish: UiFish? = null
    private var _fishSizes: List<UiFishSize>? = null
    private var onWeightChanged: (fishId: String, size: UiFishSize, weigh: String?) -> Unit =
        { _, _, _ -> }
    private var onPriceChanged: (fishId: String, size: UiFishSize, price: String?) -> Unit =
        { _, _, _ -> }
//    private var _feedWeight: String? = null
//    private var _gainWeight: String? = null
//    private var _ratio: BigDecimal? = null
//    private var _index: Int = -1

    private val binding = customViewBinding(FfrProductionInputItemViewBinding::inflate)
    private val rootView: ViewGroup
        get() = this

    fun init(
        fish: UiFish,
        sizes: List<UiFishSize>?,
        onWeightChanged: (fishId: String, size: UiFishSize, weigh: String?) -> Unit,
        onPriceChanged: (fishId: String, size: UiFishSize, price: String?) -> Unit,
    ) {
        if (_fish == fish && _fishSizes == sizes) {
            return
        }

        _fish = fish
        _fishSizes = sizes
        this.onWeightChanged = onWeightChanged
        this.onPriceChanged = onPriceChanged

        updateFishNameUi()
        addFishSizeInputViews()
        setSubtotalWeight(BigDecimal.ZERO)
        setSubtotalPrice(BigDecimal.ZERO)
    }

    fun setWeight(weight: String?, size: UiFishSize) {
        rootView.findChildByFishSize(size)?.setWeight(weight)
    }

    fun setPrice(price: String?, size: UiFishSize) {
        rootView.findChildByFishSize(size)?.setPrice(price)
    }

    fun setSubtotalWeight(subtotalWeight: BigDecimal) {
        binding.subtotalWeightTextView.text =
            resources.getString(R.string.formatted_viss, numberFormat.format(subtotalWeight))
    }

    fun setSubtotalPrice(subtotalPrice: BigDecimal) {
        binding.subtotalPriceTextView.text =
            resources.getString(R.string.format_kyat, numberFormat.format(subtotalPrice))
    }

    private fun ViewGroup.findChildByFishSize(size: UiFishSize): ProductionSizeInputItemView? {
        this.children.forEach { child ->
            if (child is ProductionSizeInputItemView && child.getSize() == size) {
                return child
            }
        }
        return null
    }


    //
//    fun setFeedWeight(weight: String?) {
//        if (_feedWeight == weight) {
//            return
//        }
//
//        _feedWeight = weight
//        updateGainWeightUi()
//        _onInputChangeListener?.onFeedWeightChanged(_index, weight)
//    }
//
//    fun setGainWeight(weight: String?) {
//        if (_gainWeight == weight) {
//            return
//        }
//
//        _gainWeight = weight
//        updateGainWeightUi()
//        _onInputChangeListener?.onGainWeightChanged(_index, weight)
//    }
//
//    fun setRatio(ratio: BigDecimal?) {
//        if (_ratio == ratio) {
//            return
//        }
//
//        _ratio = ratio
//        updateRatioUi()
//        _onInputChangeListener?.onRatioChanged(_index, ratio)
//    }
//
    private fun updateFishNameUi() {
        binding.fishNameTextView.text = _fish?.name.orEmpty()
    }

    private fun addFishSizeInputViews() {
        binding.fishSizeContainer.removeAllViews()
        _fishSizes.orEmpty().forEach { fishSize ->
            binding.fishSizeContainer.addView(
                createFishSizeInputView(fishSize)
            )
        }
    }

    private fun createFishSizeInputView(size: UiFishSize) =
        ProductionSizeInputItemView(context).apply {
            init(
                size = size,
                onWeightChanged = { size, weight ->
                    _fish?.let { fish ->
                        onWeightChanged(fish.id, size, weight)
                    }
                },
                onPriceChanged = { size, price ->
                    _fish?.let { fish ->
                        onPriceChanged(fish.id, size, price)
                    }
                }
            )
        }

    //    private fun updateFeedWeightUi() {
//        binding.feedWeightTextInputEditText.bindText(_feedWeight)
//    }
//
//    private fun updateGainWeightUi() {
//        binding.gainWeightTextInputEditText.bindText(_gainWeight)
//    }
//
//    private fun updateRatioUi() {
//        binding.ratioTextView.text = resources.getString(
//            R.string.ffr_add_edit_fcr_formatted_label_fcr,
//            numberFormat.format(_ratio.orZero())
//        )
//        binding.ratioTextView.isVisible = _ratio != null
//    }
//
    fun getFish() = _fish

    fun getFishSizes() = _fishSizes


//    fun getRatio() = _ratio
//
//    fun bind(item: UiFcr) {
//        if (_item == item) {
//            return
//        }
//
////        binding.fishName.text = fish.name
////        binding.fishSpecies.text = fish.species
////        binding.fishSpecies.isVisible = fish.species.isNotEmpty()
////        bindImage(fish.imageUrl)
//    }
//
//    fun setOnInputChangeListener(listener: OnFcrRatioInputChangeListener) {
//        _onInputChangeListener = listener
//    }
//
//    fun setErrors(error: FcrRatioInputErrorUiState?) {
//        setFeedWeightError(error?.feedWeightError)
//        setGainWeightError(error?.gainWeightError)
//    }
//
//    private fun setFeedWeightError(error: Text?) {
//        binding.feedWeightTextInputLayout.setError(error)
//    }
//
//    private fun setGainWeightError(error: Text?) {
//        binding.gainWeightTextInputLayout.setError(error)
//    }
//
}