package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import greenway_myanmar.org.databinding.FfrProductionSizeInputItemViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.kotlin.customViewBinding

class ProductionSizeInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding = customViewBinding(FfrProductionSizeInputItemViewBinding::inflate)

    private var _size: UiFishSize? = null
    private var _weight: String? = null
    private var _price: String? = null
    private var onWeightChanged: (size: UiFishSize, weight: String?) -> Unit = { _, _ -> }
    private var onPriceChanged: (size: UiFishSize, price: String?) -> Unit = { _, _ -> }

    init {
        binding.weightTextInputEditText.doAfterTextChanged {
            notifyWeightChanged(it?.toString())
        }
        binding.priceTextInputEditText.doAfterTextChanged {
            notifyPriceChanged(it?.toString())
        }
    }

    fun init(
        size: UiFishSize,
        onWeightChanged: (size: UiFishSize, weight: String?) -> Unit,
        onPriceChanged: (size: UiFishSize, price: String?) -> Unit,
    ) {
        _size = size
        this.onWeightChanged = onWeightChanged
        this.onPriceChanged = onPriceChanged
        updateSizeUi()
    }

    fun setSize(size: UiFishSize) {
        if (_size == size) {
            return
        }

        _size = size
        updateSizeUi()
    }

    fun setWeight(weight: String?) {
        if (_weight == weight) {
            return
        }

        _weight = weight
        updateWeightUi()
    }

    fun setPrice(price: String?) {
        if (_price == price) {
            return
        }

        _price = price
        updatePriceUi()

    }

    fun getSize() = _size

    fun getWeight() = _weight

    fun getPrice() = _price

    private fun updateSizeUi() {
        _size?.labelResId?.let { resId ->
            binding.sizeTextInputEditText.setText(resId)
        }
    }

    private fun updateWeightUi() {
        binding.weightTextInputEditText.bindText(_weight)
    }

    private fun updatePriceUi() {
        binding.priceTextInputEditText.bindText(_weight)
    }

    private fun notifyWeightChanged(weight: String?) {
        _size?.let { size ->
            onWeightChanged(size, weight)
        }
    }

    private fun notifyPriceChanged(price: String?) {
        _size?.let { size ->
            onPriceChanged(size, price)
        }
    }

}