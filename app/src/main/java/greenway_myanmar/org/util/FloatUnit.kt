package greenway_myanmar.org.util

import com.google.gson.annotations.SerializedName

data class FloatUnit(
  @SerializedName("value") val value: Float,
  @SerializedName("unit") val unit: String
)
