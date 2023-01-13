package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditpond

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenwaymyanmar.common.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.CreateNewPondUseCase.CreateNewPondResult
import greenway_myanmar.org.features.fishfarmrecord.domain.usecase.validation.PondNameValidationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditPondViewModel @Inject constructor(
    private val pondNameValidationUseCase: PondNameValidationUseCase,
    private val createNewPondUseCase: CreateNewPondUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(AddEditPondUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUiState: AddEditPondUiState
        get() = uiState.value

    fun handleEvent(event: AddEditPondEvent) {
        when (event) {
            is AddEditPondEvent.OnPondNameChanged -> {
                updatePondName(event.pondName)
            }
            is AddEditPondEvent.OnPondAreaChanged -> {
                updatePondArea(event.pondArea)
            }
            is AddEditPondEvent.OnDetailChanged -> {
                updateDetail(event.showDetail)
            }
            is AddEditPondEvent.OnPondDepthChanged -> {
                updatePondDepth(event.pondDepth)
            }
            is AddEditPondEvent.OnPondOwnershipChanged -> {

            }
            is AddEditPondEvent.OnSubmit -> {
                onSubmit()
            }
            AddEditPondEvent.OnCreatedPondHandled -> {
                onCreatedPondHandled()
            }
        }
    }

    private fun updatePondName(pondName: String?) {
        if (currentUiState.pondName == pondName) {
            return
        }

        _uiState.update {
            it.copy(pondName = pondName)
        }
    }

    private fun updatePondDepth(pondDepth: String?) {
        if (currentUiState.pondDepth == pondDepth) {
            return
        }

        _uiState.update {
            it.copy(pondDepth = pondDepth)
        }
    }

    private fun updatePondArea(pondArea: String?) {
        if (currentUiState.pondArea == pondArea) {
            return
        }

        _uiState.update {
            it.copy(pondArea = pondArea)
        }
    }

    private fun updateDetail(showDetail: Boolean) {
        if (currentUiState.isDetail == showDetail) {
            return
        }

        _uiState.update {
            it.copy(isDetail = showDetail)
        }
    }

    private fun onCreatedPondHandled() {
        _uiState.update {
            it.copy(createdPond = null)
        }
    }

    private fun onSubmit() {
        val nameUseCase = pondNameValidationUseCase(currentUiState.pondName)

        val pondName = if (nameUseCase is ValidationResult.Success) {
            nameUseCase.data
        } else {
            ""
        }

        val pondNameError = if (nameUseCase is ValidationResult.Error) {
            nameUseCase.message
        } else {
            null
        }

        _uiState.update {
            it.copy(
                pondNameError = pondNameError
            )
        }

        val hasError = listOf(nameUseCase).any { !it.isSuccessful }
        if (hasError) {
            return
        }

        viewModelScope.launch {
            val result = createNewPondUseCase(
                CreateNewPondUseCase.CreateNewPondRequest(
                    pondName = pondName
                )
            )
            when (result) {
                is Result.Error -> {

                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    onNewPondCreated(result.data)
                }
            }
        }
    }

    private fun onNewPondCreated(result: CreateNewPondResult) {
        _uiState.update {
            it.copy(createdPond = AddEditPondUiState.CreatedPond(pondId = result.pondId))
        }
    }

}