package greenway_myanmar.org.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import greenway_myanmar.org.R
import greenway_myanmar.org.util.MyanmarZarConverter
import greenway_myanmar.org.util.extensions.orZero
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import java.math.BigDecimal
import java.text.NumberFormat

class GreenWayMoneyTextView : GreenWayTextView {

  private val currencyFormat: NumberFormat = NumberFormat.getInstance(MyanmarZarConverter.getLocale())

  constructor(context: Context) : super(context) {
    init(context, null, 0)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init(context, attrs, 0)
  }
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
    init(context, attrs, defStyleAttr)
  }

  private var _amount: BigDecimal = BigDecimal(0)
  private var _showSymbol = true

  private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    processAttributes(context, attrs, defStyleAttr)
  }

  private fun processAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    val a: TypedArray =
      context.obtainStyledAttributes(attrs, R.styleable.GreenWayMoneyTextView, defStyleAttr, 0)
    if (a.hasValue(R.styleable.GreenWayMoneyTextView_amount)) {
      setAmount(a.getFloat(R.styleable.GreenWayMoneyTextView_amount, 0f))
    }
    if (a.hasValue(R.styleable.GreenWayMoneyTextView_showSymbol)) {
      showSymbol(a.getBoolean(R.styleable.GreenWayMoneyTextView_showSymbol, true))
    }
    a.recycle()
  }

  fun showSymbol(show: Boolean) {
    _showSymbol = show
    invalidate()
  }

  private fun setAmount(price: Float) {
    setAmount(price.toBigDecimal())
  }

  fun setAmount(price: String) {
    setAmount(price.toBigDecimalOrZero())
  }

  fun setAmount(price: BigDecimal?) {
    _amount = price.orZero()

    val stringRes =
      if (_showSymbol) {
        R.string.format_kyat
      } else {
        R.string.myanmar_kyat_without_symbol
      }

    text = resources.getString(stringRes).format(currencyFormat.format(_amount))
  }
}
