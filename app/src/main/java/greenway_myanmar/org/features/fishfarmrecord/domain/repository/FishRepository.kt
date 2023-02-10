package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import kotlinx.coroutines.flow.Flow

interface FishRepository {
    fun getFishesStream(query: String, forceRefresh: Boolean = false): Flow<Result<List<Fish>>>
}