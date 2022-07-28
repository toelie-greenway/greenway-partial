package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.GreenWayLargeSwitchOptionInputViewBinding

class GreenWayLargeSwitchOptionInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  LinearLayout(context, attrs, defStyleAttr) {

  private var _label: String = ""
  private var _text: String = ""
  private var _textPlaceholder: String = ""

  private var _textAppearance = 0

  private var _clickCallback: ClickCallback? = null

  private val binding =
    GreenWayLargeSwitchOptionInputViewBinding.inflate(LayoutInflater.from(context), this, true)

  init {
    context.theme
      .obtainStyledAttributes(attrs, R.styleable.GreenWayLargeSwitchOptionInputView, 0, 0)
      .apply {
        try {
          val textAppearance =
            getResourceId(R.styleable.GreenWayLargeSwitchOptionInputView_android_textAppearance, 0)
          setTextAppearance(textAppearance)

          val label = getString(R.styleable.GreenWayLargeSwitchOptionInputView_android_label) ?: ""
          if (label.isNotEmpty()) {
            setLabel(label)
          }

          _textPlaceholder =
            getString(R.styleable.GreenWayLargeSwitchOptionInputView_textPlaceholder) ?: ""

          val text = getString(R.styleable.GreenWayLargeSwitchOptionInputView_android_text) ?: ""
          setText(text)
        } finally {
          recycle()
        }
      }

    binding.root.setOnClickListener {

      //      _expanded = !_expanded
      //      toggleDropdownArrow()
      //
      //      if (_expanded) {
      //        if (_items.isEmpty()) {
      //          _clickCallback?.loadItem()
      //        } else {
      //          showDropdown()
      //        }
      //      }
    }
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

  fun setTextPlaceholder(placeholder: String) {
    _textPlaceholder = placeholder
    setText(_text)
  }

  fun setText(text: String) {
    val colorResId = if (text.isNotEmpty()) R.color.app_primary_text else R.color.app_secondary_text
    binding.textTextView.setTextColor(ContextCompat.getColor(context, colorResId))
    _text = text.ifEmpty { _textPlaceholder }
    binding.textTextView.text = _text
  }

  fun setClickCallback(clickCallback: ClickCallback?) {
    _clickCallback = clickCallback
  }

  interface ClickCallback {
    fun onCheckChanged(selected: Boolean)
  }
}
