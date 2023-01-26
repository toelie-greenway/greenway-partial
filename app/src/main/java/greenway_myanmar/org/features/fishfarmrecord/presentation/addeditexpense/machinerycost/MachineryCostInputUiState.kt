package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.machinerycost

import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiMachineryCost

data class MachineryCostInputUiState(
    val machineCost: String? = null,

    val machineCostError: Text? = null,

    val inputResult: UiMachineryCost? = null
)