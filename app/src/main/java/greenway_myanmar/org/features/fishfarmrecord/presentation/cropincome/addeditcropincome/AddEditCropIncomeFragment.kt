package greenway_myanmar.org.features.fishfarmrecord.presentation.cropincome.addeditcropincome

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.presentation.extensions.hideSoftInput
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.FfrAddEditCropIncomeFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiCrop
import greenway_myanmar.org.ui.croppicker.CropPickerFragment
import greenway_myanmar.org.ui.widget.GreenWayDateInputView.OnDateChangeListener
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.requireNetworkConnection
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class AddEditCropIncomeFragment : Fragment(R.layout.ffr_add_edit_crop_income_fragment) {

    private val viewModel: AddEditCropIncomeViewModel by viewModels()
    private val binding by viewBinding(FfrAddEditCropIncomeFragmentBinding::bind)
    private val args by navArgs<AddEditCropIncomeFragmentArgs>()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenTransactionName(view)
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupDateInputUi()
        setupCropInputUi()
        setupPriceInputUi()
        setupSubmitButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeDate()
            observeCrop()
            observeCropError()

            observePrice()
            observePriceError()

            observeSavingState()
        }
    }

    private fun setupTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    private fun setScreenTransactionName(view: View) {
        ViewCompat.setTransitionName(
            view,
            getString(R.string.ffr_transition_name_screen_add_edit_expense)
        )
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(
            CropPickerFragment.REQUEST_KEY_CROP
        ) { _, bundle ->
            val crop = bundle.getParcelableExtraCompat<UiCrop>(
                CropPickerFragment.KEY_CROP
            )
            if (crop != null) {
                viewModel.handleEvent(
                    AddEditCropIncomeEvent.OnCropChanged(crop)
                )
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupDateInputUi() {
        binding.dateInputView.onDateChangeListener = object : OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(AddEditCropIncomeEvent.OnDateChanged(date))
            }
        }
    }

    private fun setupCropInputUi() {
        binding.cropTextInputEditText.setOnClickListener {
            openCropPickerDialog()
        }
    }

    private fun setupPriceInputUi() {
        binding.priceTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(
                AddEditCropIncomeEvent.OnPriceChanged(
                    it?.toString().orEmpty()
                )
            )
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            hideSoftInput()
            requireNetworkConnection {
                viewModel.handleEvent(AddEditCropIncomeEvent.OnSubmit)
            }
        }
    }

    private fun CoroutineScope.observeDate() = launch {
        viewModel.uiState.map { it.date }
            .distinctUntilChanged()
            .collect {
                binding.dateInputView.bindDate(it)
            }
    }

    private fun CoroutineScope.observeCrop() = launch {
        viewModel.uiState.map { it.crop }
            .distinctUntilChanged()
            .collect {
                binding.cropTextInputEditText.bindText(it?.name)
            }
    }

    private fun CoroutineScope.observeCropError() = launch {
        viewModel.uiState.map { it.cropError }
            .distinctUntilChanged()
            .collect {
                binding.cropTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observePrice() = launch {
        viewModel.uiState.map { it.price }
            .distinctUntilChanged()
            .collect {
                binding.priceTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observePriceError() = launch {
        viewModel.uiState.map { it.priceError }
            .distinctUntilChanged()
            .collect {
                binding.priceTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeSavingState() = launch {
        launch {
            viewModel.cropIncomeSavingUiState
                .collect { uiState ->
                    binding.submitButton.isVisible = uiState !is LoadingState.Loading
                    binding.savingStateContainer.isVisible = uiState is LoadingState.Loading
                    when (uiState) {
                        is LoadingState.Success -> {
                            navController.popBackStack()
                        }
                        is LoadingState.Error -> {
                            if (uiState.message != null) {
                                showCropIncomeSavingError(uiState.message)
                            }
                        }
                        else -> {
                            /* no-op */
                        }
                    }

                }
        }
    }

    private fun openCropPickerDialog() {
        findNavController().navigate(
            AddEditCropIncomeFragmentDirections.actionAddEditCropIncomeFragmentToCropPickerFragment()
        )
    }

    private fun showCropIncomeSavingError(message: Text) {
        showSnackbar(message)
        viewModel.handleEvent(AddEditCropIncomeEvent.OnCropIncomeSavingErrorShown)
    }
}
