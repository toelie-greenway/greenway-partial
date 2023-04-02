package greenway_myanmar.org.vo

import androidx.annotation.StringDef
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.greenwaymyanmar.common.data.api.v1.gson.Exclude
import greenway_myanmar.org.db.converter.ImageListConverter
import greenway_myanmar.org.util.MyanmarZarConverter
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Entity
@TypeConverters(
  ImageListConverter::class,
)
data class Thread(
  @PrimaryKey @SerializedName("id") var id: String,
  @SerializedName("title") var title: String? = null,
  @SerializedName("body") var body: String? = null,
  @SerializedName("replies_count") var replyCount: Int = 0,
  @SerializedName("view_count") var viewCount: Int = 0,
  @SerializedName("creator") @Embedded(prefix = "user_") var user: User? = null,
  @SerializedName("images") var images: List<Image>? = null,
  @SerializedName("created_at") var createdAt: Date? = null,
  @SerializedName("client") var clientId: String? = null,
  @Exclude var status: String? = null,
  @SerializedName("is_loved") var isLoved: Boolean = false,
  @SerializedName("is_liked") var isLiked: Boolean = false,
  @SerializedName("is_solved") var isSolved: Boolean = false,

  // Extensions
  @SerializedName("is_complete_question") var isCompleteQuestion: Boolean = false,
  @SerializedName("crop_life") var cropLife: Double = 0.0,
  @SerializedName("is_applied_fertilizer") var isAppliedFertilizer: Boolean? = null,
  @SerializedName("is_applied_pesticide") var isAppliedPesticide: Boolean? = null,
  @SerializedName("weathers") var weathers: List<String>? = null,

  // Tag Vote Options
) {
  val prettyTime: String
    get() {
      if (createdAt == null) return ""
      val t = PrettyTime(Date(), Locale("MM"))
      return MyanmarZarConverter.toMyanmarNumber(t.format(createdAt))
    }

  @Retention(AnnotationRetention.SOURCE)
  @StringDef(STATUS_PENDING, STATUS_DELETING, STATUS_UPDATING)
  annotation class Status

  companion object {
    const val STATUS_PENDING = "pending"
    const val STATUS_DELETING = "deleting"
    const val STATUS_UPDATING = "updating"
  }
}
