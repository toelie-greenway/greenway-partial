package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import kotlinx.coroutines.flow.Flow

interface FishRepository {
    fun getFishesStream(query: String): Flow<List<Fish>>
}