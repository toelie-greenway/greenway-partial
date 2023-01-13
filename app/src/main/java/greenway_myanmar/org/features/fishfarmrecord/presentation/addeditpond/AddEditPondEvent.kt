package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditpond

import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiPondOwnership

sealed class AddEditPondEvent {
    data class OnDetailChanged(val showDetail: Boolean) : AddEditPondEvent()
    data class OnPondNameChanged(val pondName: String?) : AddEditPondEvent()
    data class OnPondAreaChanged(val pondArea: String?) : AddEditPondEvent()
    data class OnPondOwnershipChanged(val ownership: UiPondOwnership?) : AddEditPondEvent()
    data class OnPondDepthChanged(val pondDepth: String?) : AddEditPondEvent()
    object OnCreatedPondHandled : AddEditPondEvent()

    object OnSubmit : AddEditPondEvent()
}
