package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.transition.TransitionManager
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.GreenWayLargeDropdownTextInputViewBinding
import greenway_myanmar.org.ui.transition.Rotate
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState.Error
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState.Loading
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState.Success
import greenway_myanmar.org.vo.Resource
import greenway_myanmar.org.vo.SingleListItem
import greenway_myanmar.org.vo.Status

class GreenWayLargeDropdownTextInputView<T : SingleListItem>
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    private var toggle: Rotate

    private var _expanded = false
    private var _label: String = ""
    private var _text: String = ""
    private var _selection: T? = null
    private var _loadingState: LoadingState<T>? = null

    private var _textAppearance = 0

    private val _items = mutableListOf<T>()

    private var _clickCallback: ClickCallback<T>? = null

    private val binding =
        GreenWayLargeDropdownTextInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.GreenWayLargeDropdownTextInputView, 0, 0)
            .apply {
                try {
                    val textAppearance =
                        getResourceId(
                            R.styleable.GreenWayLargeDropdownTextInputView_android_textAppearance,
                            0
                        )
                    setTextAppearance(textAppearance)

                    val label =
                        getString(R.styleable.GreenWayLargeDropdownTextInputView_android_label)
                            ?: ""
                    if (label.isNotEmpty()) {
                        setLabel(label)
                    }

                    val text =
                        getString(R.styleable.GreenWayLargeDropdownTextInputView_android_text) ?: ""
                    setText(text)
                } finally {
                    recycle()
                }
            }

        toggle = Rotate()
        toggle.addTarget(binding.expandIcon)

        binding.root.setOnClickListener {
            if (_loadingState == Loading) {
                // ignore click when loading data
                return@setOnClickListener
            }

            _expanded = !_expanded
            toggleDropdownArrow()

            if (_expanded) {
                if (_items.isEmpty()) {
                    _clickCallback?.loadItem()
                } else {
                    showDropdown()
                }
            }
        }
    }

    private fun toggleDropdownArrow(resetAnimationDuration: Long = 200L) {
        toggle.duration = if (_expanded) 300L else resetAnimationDuration
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, toggle)
        binding.expandIcon.rotation = if (_expanded) 180f else 0f
    }

    private fun showDropdown() {
        val menu = PopupMenu(context, binding.root)
        _items.forEachIndexed { index, item ->
            menu.menu.add(
                0,
                item.itemId.toInt(),
                index,
                item.displayText
            )
        }
        menu.setOnMenuItemClickListener { menuItem ->
            _items.firstOrNull { it.itemId.toInt() == menuItem.itemId }?.let { selectedItem ->
                setSelection(selectedItem)
                _clickCallback?.onItemSelected(selectedItem)
            }
            true
        }
        menu.setOnDismissListener { resetExpanded() }
        menu.show()
    }

    fun setTextAppearance(textAppearance: Int) {
        if (textAppearance != 0 && _textAppearance != textAppearance) {
            _textAppearance = textAppearance
            TextViewCompat.setTextAppearance(binding.textTextView, _textAppearance)
        }
    }

    fun setLabel(label: String) {
        _label = label
        binding.labelTextView.text = _label
    }

    fun setSelection(item: T?) {
        _selection = item
        setText(item?.displayText.orEmpty())
    }

    fun setText(text: String) {
        val colorResId =
            if (text.isNotEmpty()) R.color.app_primary_text else R.color.app_secondary_text
        binding.textTextView.setTextColor(ContextCompat.getColor(context, colorResId))
        _text = text.ifEmpty { resources.getString(R.string.label_choose) }
        binding.textTextView.text = _text
    }

    fun setData(state: LoadingState<T>, showDropdown: Boolean = false) {
        _loadingState = state
        binding.loadingIndicator.isVisible = state == Loading
        binding.expandIcon.isVisible = state != Loading
        binding.errorTextView.isVisible = state is Error
        when (state) {
            is LoadingState.Idle -> {}
            is Loading -> {}
            is Success -> {
                setItems(state.data, showDropdown)
            }
            is Error -> {
                binding.errorTextView.text = state.message
                resetExpanded(resetAnimationDuration = 0L)
            }
        }
    }

    private fun resetExpanded(resetAnimationDuration: Long = 200L) {
        _expanded = false
        toggleDropdownArrow(resetAnimationDuration)
    }

    private fun setItems(data: List<T>, showDropdown: Boolean = false) {
        _items.clear()
        _items.addAll(data)

        if (showDropdown) {
            showDropdown()
        }
    }

    fun setClickCallback(clickCallback: ClickCallback<T>?) {
        _clickCallback = clickCallback
    }

    sealed class LoadingState<out T> {
        object Idle : LoadingState<Nothing>()
        data class Success<out T>(val data: List<T>) : LoadingState<T>()
        data class Error(val message: String) : LoadingState<Nothing>()
        object Loading : LoadingState<Nothing>()

        override fun toString(): String {
            return when (this) {
                is Success<*> -> "Success[data=$data]"
                is Error -> "Error[message=$message]"
                Loading -> "Loading"
                Idle -> "Idle"
            }
        }

        companion object {
            fun <T> fromResource(resource: Resource<List<T>>?): LoadingState<T> {
                return when {
                    resource == null -> Idle
                    resource.status == Status.LOADING -> Loading
                    resource.status == Status.SUCCESS -> Success(resource.data ?: emptyList())
                    resource.status == Status.ERROR -> Error(resource.message.orEmpty())
                    else -> Idle
                }
            }
        }
    }

    interface ClickCallback<T> {
        fun loadItem()
        fun onItemSelected(item: T)
    }
}
