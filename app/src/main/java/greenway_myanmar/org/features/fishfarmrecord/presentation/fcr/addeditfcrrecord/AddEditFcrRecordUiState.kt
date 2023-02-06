package greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.fcr.addeditfcrrecord.views.FcrRatioInputErrorUiState
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import java.math.BigDecimal
import java.time.LocalDate

data class AddEditFcrRecordUiState(
    val date: LocalDate = LocalDate.now(),
    val fishes: List<UiFish> = emptyList(),
    val feedWeights: Map<Int, String?> = emptyMap(),
    val gainWeights: Map<Int, String?> = emptyMap(),
    val calculatedRatios: Map<Int, BigDecimal?> = emptyMap(),

    val dateError: Text? = null,

    val individualInputErrors: Map<Int, FcrRatioInputErrorUiState?>? = null,
    val allInputError: Text? = null,

    val addEditFcrRecordResult: AddEditFcrRecordResult? = null,
) {
    data class AddEditFcrRecordResult(
        val recordId: String
    )
}
