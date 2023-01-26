package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import greenway_myanmar.org.databinding.GreenWayFishListInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.UIUtils

class GreenWayFishListInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _onDataSetChangeListener: OnDataSetChangeListener = NoOpOnDataSetChangeListener()
    private var _onItemClickListener: OnItemClickListener = NoOpOnItemClickListener()

    private val binding: GreenWayFishListInputViewBinding =
        GreenWayFishListInputViewBinding.inflate(
            LayoutInflater.from(context),
            this
        )

    private val rootView: ViewGroup
        get() = binding.container

    private val _items = mutableListOf<UiFish>()

    init {
        orientation = VERTICAL
        binding.fishPickerButton.setOnClickListener {
            _onItemClickListener.onAddNewFishClick()
        }
    }

    fun setItems(newItems: List<UiFish>) {
        if (_items == newItems) {
            return
        }

        _items.clear()
        _items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun notifyDataSetChanged() {
        rootView.removeAllViews()
        _items.forEachIndexed { index, uiFish ->
            if (index > 0) {
                rootView.addView(space8dp())
            }

            val itemView = createFishCardView(uiFish)
            rootView.addView(itemView)
        }

        _onDataSetChangeListener.onDataSetChanged(_items)
    }

    private fun createFishCardView(item: UiFish): View = GreenWayFishInputItemView(context).apply {
        this.setFish(item)
    }

    private fun space8dp() = Space(context).apply {
        layoutParams = LayoutParams(0, UIUtils.dpToPx(context, 8))
    }

    fun setOnDataSetChangeListener(listener: OnDataSetChangeListener) {
        _onDataSetChangeListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        _onItemClickListener = listener
    }

    interface OnDataSetChangeListener {
        fun onDataSetChanged(items: List<UiFish>)
    }

    interface OnItemClickListener {
        fun onAddNewFishClick()
        fun onFishItemClick()
    }

    private class NoOpOnDataSetChangeListener : OnDataSetChangeListener {
        override fun onDataSetChanged(items: List<UiFish>) {
            // no-op
        }
    }

    private class NoOpOnItemClickListener : OnItemClickListener {
        override fun onAddNewFishClick() {
            // no-op
        }

        override fun onFishItemClick() {
            // no-op
        }

    }
}