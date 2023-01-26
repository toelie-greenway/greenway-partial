package greenway_myanmar.org.db.converter

import androidx.room.TypeConverter
import com.greenwaymyanmar.vo.PendingAction

object PendingActionConverter {

  private val pendingActionValues by lazy(LazyThreadSafetyMode.NONE) { PendingAction.values() }

  @TypeConverter @JvmStatic fun fromPendingAction(action: PendingAction?): String? = action?.value

  @TypeConverter
  @JvmStatic
  fun toPendingAction(action: String?) = pendingActionValues.firstOrNull { it.value == action }
}
