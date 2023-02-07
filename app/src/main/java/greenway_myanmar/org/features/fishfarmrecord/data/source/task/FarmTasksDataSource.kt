package greenway_myanmar.org.features.fishfarmrecord.data.source.task

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import greenway_myanmar.org.features.fishfarmrecord.data.source.task.util.setNetworkConstraint
import greenway_myanmar.org.features.fishfarmrecord.data.source.task.workers.PostFarmWorker
import javax.inject.Inject

class FarmTasksDataSource @Inject constructor(
    private val workManager: WorkManager
) {

    fun postFarm(farmId: String) {
        val tag = PostFarmWorker.buildTag(farmId)
        val request = OneTimeWorkRequest.Builder(PostFarmWorker::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(tag)
            .setInputData(PostFarmWorker.buildData(farmId))
            .setNetworkConstraint()
            .build()
        workManager.enqueueUniqueWork(tag, ExistingWorkPolicy.KEEP, request)
    }
}