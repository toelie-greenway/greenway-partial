package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense.machinerycost

sealed class MachineryCostInputEvent {
    data class OnMachineryCostChanged(val total: String) : MachineryCostInputEvent()
    object OnSubmit : MachineryCostInputEvent()
}
