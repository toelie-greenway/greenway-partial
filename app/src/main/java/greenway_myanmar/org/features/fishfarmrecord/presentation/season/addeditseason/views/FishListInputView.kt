package greenway_myanmar.org.features.fishfarmrecord.presentation.season.addeditseason.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.isVisible
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.FfrFishListInputViewBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.UIUtils

class FishListInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _onDataSetChangeListener: OnDataSetChangeListener = NoOpOnDataSetChangeListener()
    private var _onItemClickListener: OnItemClickListener = NoOpOnItemClickListener()

    private val binding: FfrFishListInputViewBinding =
        FfrFishListInputViewBinding.inflate(
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

            val itemView = createFishCardView(
                item = uiFish,
                onFishRemoved = {
                    _onItemClickListener.onFishRemoved(it)
                })
            rootView.addView(itemView)
        }

        _onDataSetChangeListener.onDataSetChanged(_items)
    }

    private fun createFishCardView(item: UiFish, onFishRemoved: (UiFish) -> Unit): View =
        FishInputItemView(context).apply {
            this.init(onFishRemoved = onFishRemoved)
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

    fun setError(error: Text?) {
        binding.errorTextView.isVisible = error != null
        binding.errorTextView.text = error?.asString(context).orEmpty()
    }

    interface OnDataSetChangeListener {
        fun onDataSetChanged(items: List<UiFish>)
    }

    interface OnItemClickListener {
        fun onAddNewFishClick()
        fun onFishItemClick()
        fun onFishRemoved(fish: UiFish)
    }

    private class NoOpOnDataSetChangeListener : OnDataSetChangeListener {
        override fun onDataSetChanged(items: List<UiFish>) {
            // no-op
        }
    }

    private class NoOpOnItemClickListener : OnItemClickListener {
        override fun onAddNewFishClick() {
            /* no-op */
        }

        override fun onFishItemClick() {
            /* no-op */
        }

        override fun onFishRemoved(fish: UiFish) {
            /* no-op */
        }
    }
}