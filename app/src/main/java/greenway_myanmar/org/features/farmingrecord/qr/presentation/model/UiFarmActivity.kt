package greenway_myanmar.org.features.farmingrecord.qr.presentation.model

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.DateUtils
import java.text.DecimalFormat
import java.time.Instant

data class UiFarmActivity(
    val activityName: String,
    val date: Instant,
    val farmInputs: List<UiFarmInput>
) {
    fun formattedFarmInputs(): String {
        val sb = StringBuilder()
        val quantityFormat = DecimalFormat("0.##")
        farmInputs.forEachIndexed { index, item ->
            if (index > 0) {
                sb.appendLine()
            }
            sb.append("${item.productName} - ${quantityFormat.format(item.quantity)} ${item.unit}")
        }
        return sb.toString()
    }

    val formattedDate: String
        get() = DateUtils.format(date, "d MMMM, yyyy")

    companion object {
        fun fromDomain(domainEntity: FarmActivity) = UiFarmActivity(
            activityName = domainEntity.categoryTitle,
            date = domainEntity.date,
            farmInputs = domainEntity.farmInputs.map { UiFarmInput.fromDomain(it) }
        )
    }
}