package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditpond

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrAddEditPondFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.home.FishFarmerRecordBookHomeFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiPondOwnership
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditPondFragment : Fragment(R.layout.ffr_add_edit_pond_fragment) {

    private val viewModel: AddEditPondViewModel by viewModels()
    private var binding: FfrAddEditPondFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
            scrimColor = Color.TRANSPARENT
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
            setPathMotion(MaterialArcMotion())
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrAddEditPondFragmentBinding.bind(view)
        ViewCompat.setTransitionName(view, getString(R.string.ffrb_transition_name_new_pond))
        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.ffrb_add_edit_pond)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_done) {
                onSubmit()
                true
            } else {
                false
            }
        }
    }

    private fun setupUi() {
        setupPondNameInput()
        setupPondArea()
        setupPondOwnership()
        setupDetailCheckbox()
        setupPondDepth()
        setupSubmitButton()
    }

    private fun setupPondNameInput() {
        binding.pondNameTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditPondEvent.OnPondNameChanged(it?.toString()))
        }
    }

    private fun setupPondArea() {
        binding.pondAreaTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditPondEvent.OnPondAreaChanged(it?.toString()))
        }
    }

    private fun setupPondOwnership() {
        binding.pondOwnershipRadioGroup.setOnCheckedChangeListener { checkedButton, checked ->
            val checkedButtonId = checkedButton.id
            val ownButtonId = R.id.pond_ownership_own_radio_button
            val rentButtonId = R.id.pond_ownership_rent_radio_button
            val newOwnership = if (checkedButtonId == ownButtonId) {
                UiPondOwnership.Own
            } else if (checkedButtonId == rentButtonId) {
                UiPondOwnership.Rent
            } else {
                null
            }
            viewModel.handleEvent(AddEditPondEvent.OnPondOwnershipChanged(newOwnership))
        }
    }

    private fun setupDetailCheckbox() {
        binding.detailCheckBox.setOnCheckedChangeListener { _, checked ->
            viewModel.handleEvent(AddEditPondEvent.OnDetailChanged(checked))
        }
    }

    private fun setupPondDepth() {
        binding.pondDepthTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditPondEvent.OnPondDepthChanged(it?.toString()))
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observePondName()
            observePondArea()
            observePondDepthVisibility()
            observeUPaingNumberVisibility()

            observePondNameError()

            observeCreatedPond()
        }
    }

    private fun CoroutineScope.observePondName() {
        launch {
            viewModel.uiState.map { it.pondName }
                .distinctUntilChanged()
                .collect {
                    binding.pondNameTextInputEditText.bindText(it)
                }
        }
    }

    private fun CoroutineScope.observePondArea() {
        launch {
            viewModel.uiState.map { it.pondArea }
                .distinctUntilChanged()
                .collect {
                    binding.pondAreaTextInputEditText.bindText(it)
                }
        }
    }

    private fun CoroutineScope.observePondOwnership() {
        launch {
            viewModel.uiState.map { it.pondOwnership }
                .distinctUntilChanged()
                .collect {
                    bindPondOwnership(it)
                }
        }
    }

    private fun bindPondOwnership(ownership: UiPondOwnership?) {
        when (ownership) {
            null -> {
                binding.pondOwnershipRadioGroup.clearCheck()
            }
            UiPondOwnership.Own -> {
                binding.pondOwnershipOwnRadioButton.isChecked = true
            }
            UiPondOwnership.Rent -> {
                binding.pondOwnershipRentRadioButton.isChecked = true
            }
        }
    }

    private fun CoroutineScope.observePondDepthVisibility() {
        launch {
            viewModel.uiState.map { it.showPondDepth }
                .distinctUntilChanged()
                .collect { show ->
                    binding.upaingNoTextInputLayout.isVisible = show
                }
        }
    }

    private fun CoroutineScope.observeUPaingNumberVisibility() {
        launch {
            viewModel.uiState.map { it.showUPaingNumber }
                .distinctUntilChanged()
                .collect { show ->
                    binding.pondDepthTextInputLayout.isVisible = show
                }
        }
    }

    private fun CoroutineScope.observePondNameError() {
        launch {
            viewModel.uiState.map { it.pondNameError }
                .distinctUntilChanged()
                .collect {
                    binding.pondNameTextInputLayout.setError(it)
                }
        }
    }

    private fun CoroutineScope.observeCreatedPond() {
        launch {
            viewModel.uiState.map { it.createdPond }
                .distinctUntilChanged()
                .collect { createdPond ->
                    if (createdPond != null) {
                        navigateBackWithResult(createdPond.pondId)
                        onHandledCreatedPond()
                    }
                }
        }
    }

    private fun navigateBackWithResult(pondId: String) {
        setFragmentResult(
            FishFarmerRecordBookHomeFragment.REQUEST_KEY_ADD_NEW_POND, bundleOf(
                FishFarmerRecordBookHomeFragment.BUNDLE_KEY_NEW_POND_CREATED to true,
                FishFarmerRecordBookHomeFragment.BUNDLE_KEY_POND_ID to pondId
            )
        )
        navController.popBackStack()
    }

    private fun onHandledCreatedPond() {
        viewModel.handleEvent(AddEditPondEvent.OnCreatedPondHandled)
    }

    private fun onSubmit() {
        viewModel.handleEvent(
            AddEditPondEvent.OnSubmit
        )
    }
}