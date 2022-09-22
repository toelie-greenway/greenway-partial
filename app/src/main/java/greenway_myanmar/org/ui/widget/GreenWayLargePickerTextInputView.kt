package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.GreenWayLargePickerTextInputViewBinding
import greenway_myanmar.org.vo.SingleListItem

class GreenWayLargePickerTextInputView<T : SingleListItem>
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  LinearLayout(context, attrs, defStyleAttr) {

  private var _label: String = ""
  private var _text: String = ""
  private var _selection: T? = null

  private var _textAppearance = 0

  private var _clickCallback: ClickCallback? = null

  private val binding =
    GreenWayLargePickerTextInputViewBinding.inflate(LayoutInflater.from(context), this, true)

  init {
    context.theme
      .obtainStyledAttributes(attrs, R.styleable.GreenWayLargePickerTextInputView, 0, 0)
      .apply {
        try {
          val textAppearance =
            getResourceId(R.styleable.GreenWayLargePickerTextInputView_android_textAppearance, 0)
          setTextAppearance(textAppearance)

          val label = getString(R.styleable.GreenWayLargePickerTextInputView_android_label) ?: ""
          if (label.isNotEmpty()) {
            setLabel(label)
          }

          val text = getString(R.styleable.GreenWayLargePickerTextInputView_android_text) ?: ""
          setText(text)
        } finally {
          recycle()
        }
      }

    binding.root.setOnClickListener {
      _clickCallback?.onClick()
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

  fun setSelection(item: T?) {
    if (_selection != null && _selection == item) {
      return
    }
    _selection = item
    setText(item?.displayText.orEmpty())
  }

  fun removeSelection() {
    setSelection(null)
  }

  fun setText(text: String) {
    val colorResId = if (text.isNotEmpty()) R.color.app_primary_text else R.color.app_secondary_text
    binding.textTextView.setTextColor(ContextCompat.getColor(context, colorResId))
    _text = text.ifEmpty { resources.getString(R.string.label_choose) }
    binding.textTextView.text = _text
  }

  fun setError(text: Text?) {
    binding.errorTextView.text = text?.asString(context).orEmpty()
    binding.errorTextView.isVisible = text != null
  }

  fun setClickCallback(clickCallback: ClickCallback) {
    _clickCallback = clickCallback
  }

  interface ClickCallback {
    fun onClick()
  }
}
