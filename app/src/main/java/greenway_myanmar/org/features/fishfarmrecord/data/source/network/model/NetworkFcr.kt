package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import com.greenwaymyanmar.core.data.json.serializers.BigDecimalAsString
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFcr (
    val fish: NetworkFish? = null,
    val total_feed: BigDecimalAsString? = null,
    val total_weight_gain: BigDecimalAsString? = null
)