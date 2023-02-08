package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.databinding.FfrFcrRatioInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.customViewBinding
import java.math.BigDecimal
import java.text.NumberFormat

private val numberFormat: NumberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())

class FcrRatioInputItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _fish: UiFish? = null
    private var _feedWeight: String? = null
    private var _gainWeight: String? = null
    private var _ratio: BigDecimal? = null
    private var _index: Int = -1

    private val binding = customViewBinding(FfrFcrRatioInputViewBinding::inflate)
    private var _onInputChangeListener: OnFcrRatioInputChangeListener? = null

    init {
        binding.feedWeightTextInputEditText.doAfterTextChanged {
            _onInputChangeListener?.onFeedWeightChanged(_index, it?.toString())
        }
        binding.gainWeightTextInputEditText.doAfterTextChanged {
            _onInputChangeListener?.onGainWeightChanged(_index, it?.toString())
        }
    }

    fun init(
        index: Int,
        fish: UiFish,
        feedWeight: String?,
        gainWeight: String?,
        ratio: BigDecimal? = null,
        onInputChangeListener: OnFcrRatioInputChangeListener?
    ) {
        if (_fish == fish && _index == index) {
            return
        }

        _fish = fish
        _index = index
        _onInputChangeListener = onInputChangeListener

        binding.fishNameTextView.text = fish.name

        if (_feedWeight != feedWeight) {
            _feedWeight = feedWeight
            updateFeedWeightUi()
        }

        if (_gainWeight != gainWeight) {
            _gainWeight = gainWeight
            updateGainWeightUi()
        }

        if (_ratio != ratio) {
            _ratio = ratio
            updateRatioUi()
        }
    }

    fun setFeedWeight(weight: String?) {
        if (_feedWeight == weight) {
            return
        }

        _feedWeight = weight
        updateGainWeightUi()
        _onInputChangeListener?.onFeedWeightChanged(_index, weight)
    }

    fun setGainWeight(weight: String?) {
        if (_gainWeight == weight) {
            return
        }

        _gainWeight = weight
        updateGainWeightUi()
        _onInputChangeListener?.onGainWeightChanged(_index, weight)
    }

    fun setRatio(ratio: BigDecimal?) {
        if (_ratio == ratio) {
            return
        }

        _ratio = ratio
        updateRatioUi()
        _onInputChangeListener?.onRatioChanged(_index, ratio)
    }

    private fun updateFeedWeightUi() {
        binding.feedWeightTextInputEditText.bindText(_feedWeight)
    }

    private fun updateGainWeightUi() {
        binding.gainWeightTextInputEditText.bindText(_gainWeight)
    }

    private fun updateRatioUi() {
        binding.ratioTextView.text = resources.getString(
            R.string.ffr_add_edit_fcr_formatted_label_fcr,
            numberFormat.format(_ratio.orZero())
        )
        binding.ratioTextView.isVisible = _ratio != null
    }

    fun getFeedWeight() = _feedWeight

    fun getGainWeight() = _gainWeight

    fun getRatio() = _ratio

    fun setOnInputChangeListener(listener: OnFcrRatioInputChangeListener) {
        _onInputChangeListener = listener
    }

    fun setErrors(error: FcrRatioInputErrorUiState?) {
        setFeedWeightError(error?.feedWeightError)
        setGainWeightError(error?.gainWeightError)
    }

    private fun setFeedWeightError(error: Text?) {
        binding.feedWeightTextInputLayout.setError(error)
    }

    private fun setGainWeightError(error: Text?) {
        binding.gainWeightTextInputLayout.setError(error)
    }

    interface OnFcrRatioInputChangeListener {
        fun onFeedWeightChanged(index: Int, weight: String?)
        fun onGainWeightChanged(index: Int, weight: String?)
        fun onRatioChanged(index: Int, ratio: BigDecimal?)
    }
}