package greenway_myanmar.org.features.fishfarmerrecordbook.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.Pond
import kotlinx.coroutines.flow.Flow

interface PondRepository {
    fun observePonds(): Flow<Result<List<Pond>>>
}