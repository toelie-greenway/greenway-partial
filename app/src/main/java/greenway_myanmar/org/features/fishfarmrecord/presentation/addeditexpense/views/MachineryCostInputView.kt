package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import greenway_myanmar.org.databinding.FfrMachineryCostInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost

class MachineryCostInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var _clickCallback: ClickCallback? = null

    private val binding: FfrMachineryCostInputViewBinding =
        FfrMachineryCostInputViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            costInputButton.setOnClickListener { _clickCallback?.onClick() }
            costCard.setOnClickListener { _clickCallback?.onClick() }
        }

    fun bind(data: UiMachineryCost?) {
        binding.costCard.isVisible = data != null
        binding.costInputButton.isVisible = data == null
        if (data != null) {
            binding.costTextView.setAmount(data.totalCost)
        }
    }

    fun setClickCall(callback: ClickCallback) {
        _clickCallback = callback
    }

    interface ClickCallback {
        fun onClick()
    }
}
