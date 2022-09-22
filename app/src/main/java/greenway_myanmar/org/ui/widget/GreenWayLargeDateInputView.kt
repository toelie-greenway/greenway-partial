package greenway_myanmar.org.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.GreenWayLargeDateInputViewBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

class GreenWayLargeDateInputView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var _label: String = ""
    private var _text: String = ""
    private var _date: Instant? = null

    private var _textAppearance = 0

    private var _clickCallback: ClickCallback? = null

    private val binding =
        GreenWayLargeDateInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.GreenWayLargeDateInputView, 0, 0)
            .apply {
                try {
                    val textAppearance =
                        getResourceId(
                            R.styleable.GreenWayLargeDateInputView_android_textAppearance,
                            0
                        )
                    setTextAppearance(textAppearance)

                    val label =
                        getString(R.styleable.GreenWayLargeDateInputView_android_label) ?: ""
                    if (label.isNotEmpty()) {
                        setLabel(label)
                    }

                    val text = getString(R.styleable.GreenWayLargeDateInputView_android_text) ?: ""
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

    fun setDate(date: Instant?) {
        if (_date != null && _date == date) {
            return
        }
        _date = date
        setText(formatDate(date))
    }

    private fun formatDate(date: Instant?): String {
        if (date == null) return ""

        val zoneId = ZoneId.systemDefault()
        val locale = try {
            Locale("my")
        } catch (e: Exception) {
            Locale.getDefault()
        }
        return DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(zoneId)
            .withDecimalStyle(DecimalStyle.of(locale))
            .format(date)
    }

    fun clear() {
        setDate(null)
    }

    fun setText(text: String) {
        val colorResId =
            if (text.isNotEmpty()) R.color.app_primary_text else R.color.app_secondary_text
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
