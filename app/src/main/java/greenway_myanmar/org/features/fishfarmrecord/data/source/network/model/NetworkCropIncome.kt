package greenway_myanmar.org.features.fishfarmrecord.data.source.network.model

import greenway_myanmar.org.features.fishfarmrecord.domain.model.CropIncome
import greenway_myanmar.org.util.extensions.toBigDecimalOrZero
import greenway_myanmar.org.util.toInstantOrNow
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCropIncome(
    val id: String,
    val date: String,
    val income: Double,
    val crop: NetworkCrop
)

fun NetworkCropIncome.asDomainModel() = CropIncome(
    id = id,
    date = date.toInstantOrNow(),
    income = income.toBigDecimalOrZero(),
    crop = crop.asDomainModel()
)