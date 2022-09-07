package greenway_myanmar.org.features.farmingrecord.qr.data.repositories

import com.greenwaymyanmar.api.ApiEmptyResponse
import com.greenwaymyanmar.api.ApiErrorResponse
import com.greenwaymyanmar.api.ApiResponse
import com.greenwaymyanmar.api.ApiSuccessResponse
import greenway_myanmar.org.common.domain.entities.ResourceError
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.farmingrecord.qr.data.api.QrService
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Season
import greenway_myanmar.org.features.farmingrecord.qr.domain.repositories.FarmRepository
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetFarmListUseCase.GetFarmListResult
import greenway_myanmar.org.features.farmingrecord.qr.domain.usecases.GetSeasonListUseCase.GetSeasonListResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFarmRepository @Inject constructor(
    private val qrService: QrService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FarmRepository {

    override suspend fun getFarmList(): GetFarmListResult {
        return withContext(ioDispatcher) {
            try {
                val response = qrService.getFarmList(1, 107)
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetFarmListResult.Success(
                            data = apiResponse.body.farms.map {
                                it.toDomain()
                            })
                    }
                    is ApiErrorResponse -> {
                        GetFarmListResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        GetFarmListResult.Success(emptyList())
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetFarmListResult.Error(ResourceError.from(e))
            }
        }
    }

    override suspend fun getSeasonList(farmId: String): GetSeasonListResult {
        return withContext(ioDispatcher) {
            try {
                val response = qrService.getSeasonList(farmId, 1, "107")
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        GetSeasonListResult.Success(
                            data = apiResponse.body.data.map {
                                it.toDomain()
                            })
                    }
                    is ApiErrorResponse -> {
                        GetSeasonListResult.Error(ResourceError.from(response))
                    }
                    is ApiEmptyResponse -> {
                        GetSeasonListResult.Success(emptyList())
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                GetSeasonListResult.Error(ResourceError.from(e))
            }
        }
    }
}