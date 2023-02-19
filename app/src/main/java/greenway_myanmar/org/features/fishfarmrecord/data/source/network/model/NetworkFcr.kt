package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fcr
import greenway_myanmar.org.util.extensions.orZero
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFcr (
    val fish: NetworkFish? = null,
    val total_feed: BigDecimalAsString? = null,
    val total_weight_gain: BigDecimalAsString? = null
)

fun NetworkFcr.asDomainModel() = Fcr(
    fish = fish?.asDomainModel() ?: throw IllegalStateException("Fish must not be null!"),
    feedWeight = total_feed.orZero(),
    gainWeight = total_weight_gain.orZero()
)