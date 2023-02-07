package greenway_myanmar.org.features.fishfarmrecord.data.source.task.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import greenway_myanmar.org.di.IoDispatcher
import greenway_myanmar.org.features.fishfarmrecord.data.source.task.util.syncForegroundInfo
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@HiltWorker
class PostFarmWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val farmRepository: FarmRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        applicationContext.syncForegroundInfo()

    override suspend fun doWork(): Result =
        withContext(ioDispatcher) {
            try {
                delay(3000)
                val farmId = inputData.getString(PARAM_FARM_ID).orEmpty()
                val newFarmId = farmRepository.postFarm(farmId)
                val success = newFarmId.isNotEmpty()
                if (success) {
                    Result.success(buildOutputData(newFarmId))
                } else {
                    notifyErrorOccurred()
                    Result.retry()
                }
            } catch (e: Exception) {
                notifyErrorOccurred()
                Result.retry()
            }
        }

    private fun buildOutputData(newFarmId: String) = Data.Builder()
        .putString(PARAM_FARM_ID, newFarmId)
        .build()

    private suspend fun notifyErrorOccurred() {
        setProgress(Data.Builder().putBoolean(KEY_IS_ERROR_OCCURRED, true).build())
    }

    companion object {
        private const val TAG = "ffr-post-farm-%s"
        private const val PARAM_FARM_ID = "farm-id"
        private const val KEY_IS_ERROR_OCCURRED = "is-error-occurred"

        fun buildData(farmId: String) = Data.Builder().putString(PARAM_FARM_ID, farmId).build()

        fun buildTag(farmId: String) = String.format(TAG, farmId)

        fun getNewFarmId(data: Data) = data.getString(PARAM_FARM_ID)

        fun isErrorOccurred(data: Data) = data.getBoolean(KEY_IS_ERROR_OCCURRED, false)
    }
}