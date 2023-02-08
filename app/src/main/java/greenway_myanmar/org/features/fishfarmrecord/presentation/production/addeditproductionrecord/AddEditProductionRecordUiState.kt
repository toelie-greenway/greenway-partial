package greenway_myanmar.org.features.fishfarmrecord.presentation.production.addeditproductionrecord

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFishSize
import java.math.BigDecimal
import java.time.LocalDate

data class AddEditProductionRecordUiState(
    val date: LocalDate = LocalDate.now(),
    val dateError: Text? = null,

    val fishes: List<UiFish>? = null,
    val fishSizes: List<UiFishSize>? = null,
    val weightsByFishAndSize: Map<Pair<String, UiFishSize>, String?> = emptyMap(),
    val pricesByFishAndSize: Map<Pair<String, UiFishSize>, String?> = emptyMap(),

    val subtotalWeightByFish: Map<String, BigDecimal> = emptyMap(),
    val subtotalPriceByFish: Map<String, BigDecimal> = emptyMap(),

    val totalWeight: BigDecimal = BigDecimal.ZERO,
    val totalPrice: BigDecimal = BigDecimal.ZERO,

    val individualInputErrorsByFishAndSize: Map<Pair<String, String>, Text?>? = null,
    val allInputError: Text? = null,
    val addEditProductionRecordResult: AddEditProductionRecordResult? = null,
) {
    data class AddEditProductionRecordResult(
        val recordId: String
    )
}