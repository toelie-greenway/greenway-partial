package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.isVisible
import greenway_myanmar.org.databinding.FfrFarmInputListInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputCost
import greenway_myanmar.org.util.UIUtils

class FarmInputListInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _onItemClickListener: OnItemClickListener = NoOpOnItemClickListener()

    private val binding: FfrFarmInputListInputViewBinding =
        FfrFarmInputListInputViewBinding.inflate(
            LayoutInflater.from(context),
            this
        )

    private val rootView: ViewGroup
        get() = binding.container

    init {
        orientation = VERTICAL
        binding.farmInputPickerButton.setOnClickListener {
            _onItemClickListener.onAddNewFarmInput()
        }
    }

    fun setItems(newItems: List<UiFarmInputCost>) {
        notifyDataSetChanged(newItems)
    }

    private fun notifyDataSetChanged(newItems: List<UiFarmInputCost>) {
        rootView.removeAllViews()
        rootView.isVisible = newItems.isNotEmpty()
        newItems.forEachIndexed { index, uiFish ->
            if (index > 0) {
                rootView.addView(space8dp())
            }

            val itemView = createFarmInputCardView(uiFish)
            rootView.addView(itemView)
        }
    }

    private fun createFarmInputCardView(item: UiFarmInputCost): View = FarmInputInputItemView(context).apply {
        this.bind(item)
    }

    private fun space8dp() = Space(context).apply {
        layoutParams = LayoutParams(0, UIUtils.dpToPx(context, 8))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        _onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onAddNewFarmInput()
        fun onFarmInputItemClick()
    }

    private class NoOpOnItemClickListener : OnItemClickListener {
        override fun onAddNewFarmInput() {
            // no-op
        }

        override fun onFarmInputItemClick() {
            // no-op
        }

    }
}