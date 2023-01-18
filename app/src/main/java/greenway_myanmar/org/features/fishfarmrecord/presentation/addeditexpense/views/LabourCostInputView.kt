package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import greenway_myanmar.org.databinding.FfrLabourCostInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLabourCost

class LabourCostInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var _clickCallback: ClickCallback? = null

    private val binding: FfrLabourCostInputViewBinding =
        FfrLabourCostInputViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            costInputButton.setOnClickListener { _clickCallback?.onClick() }
            costCard.setOnClickListener { _clickCallback?.onClick() }
        }

    var labourCost: UiLabourCost? = null
        set(value) {
            if (field != value) {
                field = value

                if (value != null) {
                    bindData(value)
                }
            }
        }

    private fun bindData(data: UiLabourCost) {

    }

    fun setClickCall(callback: ClickCallback) {
        _clickCallback = callback
    }

//    override fun setMandatory(mandatory: Boolean) {
//        _mandatory = mandatory
//    }
//
//    override fun isMandatory(): Boolean = _mandatory
//
//    override fun getValue(): AsymtLabourCost? {
//        return labourCost
//    }
//
//    override fun validate(): Boolean {
//        return if (_mandatory) _mandatory && labourCost != null else true
//    }
//
//    override fun isEmpty(): Boolean {
//        return labourCost == null
//    }
//
//    override fun showError() {
//        // no-op
//    }
//
//    override fun clearError() {
//        // no-op
//    }

    interface ClickCallback {
        fun onClick()
    }
}
