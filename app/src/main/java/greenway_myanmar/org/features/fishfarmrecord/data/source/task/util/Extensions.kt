package greenway_myanmar.org.features.fishfarmrecord.data.source.task.util

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest

fun OneTimeWorkRequest.Builder.setNetworkConstraint() = this.setConstraints(buildNetworkConstraint())

private fun buildNetworkConstraint(): Constraints {
    return Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
}