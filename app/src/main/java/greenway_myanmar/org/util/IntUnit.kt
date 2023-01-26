package greenway_myanmar.org.util

import com.google.gson.annotations.SerializedName

data class IntUnit(
  @SerializedName("value") val value: Int,
  @SerializedName("unit") val unit: String
)
