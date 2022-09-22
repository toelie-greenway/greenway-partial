package greenway_myanmar.org.util.extensions

import greenway_myanmar.org.util.MyanmarZarConverter
import java.text.DecimalFormat

fun Double.toMyanmar(): String {
  val df = DecimalFormat("#.#")
  return MyanmarZarConverter.toMyanmarNumber(df.format(this))
}
