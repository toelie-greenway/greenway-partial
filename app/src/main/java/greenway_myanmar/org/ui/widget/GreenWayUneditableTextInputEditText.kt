package greenway_myanmar.org.ui.widget

import android.content.Context
import android.text.InputType
import android.text.method.MovementMethod
import android.util.AttributeSet
import greenway_myanmar.org.common.presentation.extensions.hideSoftInput

open class GreenWayUneditableTextInputEditText : GreenWayTextInputEditText {

  constructor(context: Context) : super(context) {
    init()
  }
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
    init()
  }

  override fun getDefaultEditable(): Boolean = false

  override fun getDefaultMovementMethod(): MovementMethod? {
    return null
  }

  private fun init() {
    inputType = InputType.TYPE_NULL
    isFocusable = true
    isLongClickable = false
    isClickable = true

    setOnFocusChangeListener { v, hasFocus ->
      if (hasFocus) {
        hideSoftInput()
        performClick()
      }
    }
  }
}
