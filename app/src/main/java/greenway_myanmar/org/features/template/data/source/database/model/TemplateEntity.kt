package greenway_myanmar.org.features.template.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greenwaymyanmar.vo.PendingAction
import java.util.*

@Entity(
    tableName = "templates"
)
data class TemplateEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("pending_action")
    val pendingAction: PendingAction = PendingAction.NOTHING
)