package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.GreenWayLargeSwitchPhoneInputViewBinding

class GreenWayLargeSwitchPhoneInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var _label: String = ""
    private var _text: String = ""
    private var _hint: String = ""
    private var _check: Boolean? = null
    private var _textAppearance = 0

    private var _clickCallback: ClickCallback? = null

    private val binding =
        GreenWayLargeSwitchPhoneInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.GreenWayLargeSwitchPhoneInputView, 0, 0)
            .apply {
                try {
                    val textAppearance =
                        getResourceId(
                            R.styleable.GreenWayLargeSwitchPhoneInputView_android_textAppearance,
                            0
                        )
                    setTextAppearance(textAppearance)

                    val label =
                        getString(R.styleable.GreenWayLargeSwitchPhoneInputView_android_label)
                            ?: ""
                    if (label.isNotEmpty()) {
                        setLabel(label)
                    }

                    _hint =
                        getString(R.styleable.GreenWayLargeSwitchPhoneInputView_android_hint)
                            ?: ""

                    val text =
                        getString(R.styleable.GreenWayLargeSwitchPhoneInputView_android_text) ?: ""
                    setText(text)
                } finally {
                    recycle()
                }
            }

        binding.optionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _clickCallback?.onCheckChanged(buttonView, isChecked)
        }

        binding.root.setOnClickListener {
            _clickCallback?.onItemClick()
        }

        binding.phoneInputEditText.doAfterTextChanged {
            _clickCallback?.onPhoneChanged(it?.toString().orEmpty().trim())
        }
    }

    fun setTextAppearance(textAppearance: Int) {
        if (textAppearance != 0 && _textAppearance != textAppearance) {
            _textAppearance = textAppearance
            // TODO: TextViewCompat.setTextAppearance(binding.textTextView, _textAppearance)
        }
    }

    fun setLabel(label: String) {
        _label = label
        binding.labelTextView.text = _label
    }

    fun setTextPlaceholder(placeholder: String) {
        _hint = placeholder
        setText(_text)
    }

    fun setChecked(checked: Boolean) {
        if (_check != null && _check == checked) {
            return
        }

        _check = checked
        binding.optionSwitch.isChecked = checked
    }

    fun setText(text: String) {
        if (_text.isNotEmpty() && _text == text) {
            return
        }

        _text = text.ifEmpty { _hint }

        val currentCursorPosition = binding.phoneInputEditText.selectionEnd
        binding.phoneInputEditText.setText(text)
        binding.phoneInputEditText.setSelection(
            binding.phoneInputEditText.calculateCursorPosition(
                _text,
                text,
                currentCursorPosition
            )
        )
    }

    fun setError(text: Text?) {
        binding.errorTextView.text = text?.asString(context).orEmpty()
        binding.errorTextView.isVisible = text != null
    }

    fun setClickCallback(clickCallback: ClickCallback?) {
        _clickCallback = clickCallback
    }

    interface ClickCallback {
        fun onPhoneChanged(phone: String)
        fun onCheckChanged(buttonView: CompoundButton, selected: Boolean)
        fun onItemClick()
    }

    private fun EditText.calculateCursorPosition(
        oldText: String?,
        text: String?,
        currentCursorPosition: Int,
    ): Int {
        val newValue = text ?: ""
        val oldValue = oldText ?: ""
        val oldValueLength = oldValue.length
        val newValueLength = newValue.length
        val newCursorPosition = when {
            oldValueLength == newValueLength -> {
                currentCursorPosition
            }
            newValueLength > oldValueLength -> {
                currentCursorPosition + (newValueLength - oldValueLength)
            }
            oldValueLength > newValueLength -> {
                currentCursorPosition - (oldValueLength - newValueLength)
            }
            else -> {
                0
            }
        }
        return if (newCursorPosition >= 0) {
            newCursorPosition
        } else {
            0
        }
    }
}
