package greenway_myanmar.org.features.fishfarmrecord.data.model

import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFcr
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFcrRecord
import greenway_myanmar.org.util.toInstantOrNow

fun NetworkFcrRecord.asEntity(seasonId: String) = FfrFcrRecordEntity(
    id = id,
    date = date.toInstantOrNow(),
    seasonId = seasonId,
    pendingAction = PendingAction.NOTHING
)