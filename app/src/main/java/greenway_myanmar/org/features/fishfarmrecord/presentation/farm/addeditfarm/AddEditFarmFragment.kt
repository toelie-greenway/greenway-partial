package greenway_myanmar.org.features.fishfarmrecord.presentation.farm.addeditfarm

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.common.imagepicker.ImagePickerLifecycleObserver
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.ui.widgets.AsylImageInputView
import com.greenwaymyanmar.ui.widgets.AsylImageInputView.OnImageInputListener
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.databinding.FfrAddEditFarmFragmentBinding
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.presentation.AreaMeasureMapFragment
import greenway_myanmar.org.features.areameasure.presentation.AreaMeasureMethodFragment
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.areameasure.presentation.model.toStaticMapUrl
import greenway_myanmar.org.features.fishfarmrecord.presentation.home.FishFarmerRecordBookHomeFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmOwnership
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getNavigationResult
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.requireNetworkConnection
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditFarmFragment : Fragment(R.layout.ffr_add_edit_farm_fragment) {

    private val viewModel: AddEditFarmViewModel by viewModels()
    private var binding: FfrAddEditFarmFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    private val imagePickerObserver: ImagePickerLifecycleObserver by lazy {
        ImagePickerLifecycleObserver(
            "ffr-farm-image",
            this,
            requireActivity().activityResultRegistry,
            imageResultListener,
        )
    }

    private val imageResultListener =
        object : ImagePickerLifecycleObserver.ImageResultListener {
            override fun onSuccess(uri: Uri) {
                onImageResult(uri)
            }

            override fun onError(error: String) {
                // no-op
            }

            override fun onCancel() {
                // no-op
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    private fun setupTransition() {
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
        binding = FfrAddEditFarmFragmentBinding.bind(view)
        ViewCompat.setTransitionName(
            view,
            getString(R.string.ffr_transition_name_screen_add_edit_farm)
        )
        registerImagePickerObserver()
        observeAreaMeasureResults()
        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun registerImagePickerObserver() {
        requireActivity().lifecycle.addObserver(imagePickerObserver)
    }

    private fun observeAreaMeasureResults() {
        getNavigationResult<AreaMeasureMethod>(
            R.id.addEditFarmFragment,
            AreaMeasureMethodFragment.RESULT_KEY_MEASURE_METHOD
        ) { result ->
            openAreaMeasureMapScreen(result)
        }
        getNavigationResult<AreaMeasurement>(
            R.id.addEditFarmFragment,
            AreaMeasureMapFragment.RESULT_KEY_MEASURE_MAP
        ) { result ->
            onAreaMeasureResult(result)
        }
    }

    private fun onAreaMeasureResult(result: AreaMeasurement) {
        viewModel.handleEvent(AddEditFarmEvent.OnFarmAreaMeasurementChanged(result))
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
        setupFarmNameInputUi()
        setupFarmLocationInputUi()
        setupFarmAreaInputUi()
        setupFarmOwnershipUi()
        setupFarmImageInputUi()
        setupDetailCheckboxUi()
        setupFarmDepthInputUi()
        setupFarmUPaingNumberInputUi()
        setupSubmitButton()
    }

    private fun setupFarmLocationInputUi() {
        binding.farmMapImageView.setOnClickListener {
            openAreaMeasureMethodScreen()
        }
    }

    private fun setupFarmNameInputUi() {
        binding.farmNameTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditFarmEvent.OnFarmNameChanged(it?.toString()))
        }
    }

    private fun setupFarmAreaInputUi() {
        binding.farmAreaTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditFarmEvent.OnFarmAreaChanged(it?.toString()))
        }
    }

    private fun setupFarmOwnershipUi() {
        binding.farmOwnershipRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val ownButtonId = R.id.farm_ownership_own_radio_button
            val rentButtonId = R.id.farm_ownership_rent_radio_button
            val newOwnership = when (checkedId) {
                ownButtonId -> {
                    UiFarmOwnership.Own
                }
                rentButtonId -> {
                    UiFarmOwnership.Rent
                }
                else -> {
                    null
                }
            }
            viewModel.handleEvent(AddEditFarmEvent.OnFarmOwnershipChanged(newOwnership))
        }
    }

    private fun setupFarmImageInputUi() {
        binding.farmImageInputView.onImageInputListener = object : OnImageInputListener {
            override fun pickImage(view: AsylImageInputView) {
                imagePickerObserver.pickImage()
            }

            override fun takePhoto(view: AsylImageInputView) {
                imagePickerObserver.takePicture()
            }

            override fun removePhoto(view: AsylImageInputView) {
                // TODO: remove url from view model
            }

        }
    }

    private fun setupDetailCheckboxUi() {
        binding.detailCheckBox.setOnCheckedChangeListener { _, checked ->
            viewModel.handleEvent(AddEditFarmEvent.OnDetailChanged(checked))
        }
    }

    private fun setupFarmDepthInputUi() {
        binding.farmDepthTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditFarmEvent.OnFarmDepthChanged(it?.toString()))
        }
    }

    private fun setupFarmUPaingNumberInputUi() {
        binding.upaingNoTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditFarmEvent.OnFarmUPaingNumberChanged(it?.toString()))
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFarmName()
            observeFarmNameError()

            observeFarmArea()
            observeFarmAreaError()

            observeFarmMeasurement()

            observeFarmImageUri()

            observeFarmOwnership()
            observeFarmOwnershipError()

            observeInputFarmDetailCheck()

            observePlotId()
            observeFarmDepth()

            observeNewFarmResult()
        }
    }

    private fun CoroutineScope.observeFarmName() = launch {
        viewModel.uiState.map { it.farmName }
            .distinctUntilChanged()
            .collect {
                binding.farmNameTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFarmNameError() {
        launch {
            viewModel.uiState.map { it.farmNameError }
                .distinctUntilChanged()
                .collect {
                    binding.farmNameTextInputLayout.setError(it)
                }
        }
    }

    private fun CoroutineScope.observeFarmArea() = launch {
        viewModel.uiState.map { it.farmArea }
            .distinctUntilChanged()
            .collect {
                binding.farmAreaTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFarmAreaError() = launch {
        launch {
            viewModel.uiState.map { it.farmAreaError }
                .distinctUntilChanged()
                .collect {
                    binding.farmAreaTextInputLayout.setError(it)
                }
        }
    }

    private fun CoroutineScope.observeFarmMeasurement() = launch {
        launch {
            viewModel.uiState.map { it.farmMeasurement }
                .distinctUntilChanged()
                .collect {
                    bindMapPreviewImage(it)
                }
        }
    }

    private fun bindMapPreviewImage(measurement: AreaMeasurement?) {
        when (measurement) {
            is AreaMeasurement.Location -> {
                showMapPreview(measurement.toStaticMapUrl(requireContext().resources))
            }
            is AreaMeasurement.Area -> {
                showMapPreview(measurement.toStaticMapUrl(requireContext().resources))
            }
            else -> {
                showMapDefaultPreview()
            }
        }
    }

    private fun CoroutineScope.observeFarmImageUri() = launch {
        viewModel.uiState.map { it.farmImageUri }
            .distinctUntilChanged()
            .collect {
                binding.farmImageInputView.imageFilePath = it?.toString().orEmpty()
            }
    }

    private fun CoroutineScope.observeFarmOwnership() = launch {
        viewModel.uiState.map { it.farmOwnership }
            .distinctUntilChanged()
            .collect {
                bindPondOwnership(it)
            }
    }

    private fun bindPondOwnership(ownership: UiFarmOwnership?) {
        when (ownership) {
            null -> {
                binding.farmOwnershipRadioGroup.clearCheck()
            }
            UiFarmOwnership.Own -> {
                binding.farmOwnershipOwnRadioButton.isChecked = true
            }
            UiFarmOwnership.Rent -> {
                binding.farmOwnershipRentRadioButton.isChecked = true
            }
        }
    }

    private fun CoroutineScope.observeFarmOwnershipError() = launch {
        launch {
            viewModel.uiState.map { it.farmOwnershipError }
                .distinctUntilChanged()
                .collect {
                    bindFarmOwnershipError(it)
                }
        }
    }

    private fun bindFarmOwnershipError(error: Text?) {
        val errorMessage = if (error != null) {
            resources.getString(R.string.error_field_required)
        } else {
            null
        }
        binding.farmOwnershipRadioGroup.children.forEach {
            if (it is RadioButton) {
                it.error = errorMessage
            }
        }
    }

    private fun CoroutineScope.observeInputFarmDetailCheck() {
        launch {
            viewModel.uiState.map { it.inputFarmDetail }
                .distinctUntilChanged()
                .collect { newValue ->
                    val oldValue = binding.detailCheckBox.isChecked
                    if (oldValue != newValue) {
                        binding.detailCheckBox.isChecked = newValue
                    }
                    binding.upaingNoTextInputLayout.isVisible = newValue
                    binding.farmDepthTextInputLayout.isVisible = newValue
                }
        }
    }

    private fun CoroutineScope.observePlotId() = launch {
        viewModel.uiState.map { it.plotId }
            .distinctUntilChanged()
            .collect {
                binding.upaingNoTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeFarmDepth() = launch {
        viewModel.uiState.map { it.farmDepth }
            .distinctUntilChanged()
            .collect {
                binding.farmDepthTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeNewFarmResult() {
        launch {
            viewModel.uiState.map { it.farmUploadState }
                .distinctUntilChanged()
                .collect { state ->
                    binding.submitButton.isVisible = state !is LoadingState.Loading
                    binding.uploadingStateContainer.isVisible = state is LoadingState.Loading
                    when (state) {
                        is LoadingState.Error -> {
                            navigateBackWithoutResult()
                        }
                        is LoadingState.Success -> {
                            navigateBackWithResult(state.data.farmId)
                        }
                        else -> {
                            /* no-op */
                        }
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

    private fun navigateBackWithoutResult() {
        navController.popBackStack()
    }

    private fun onSubmit() {
        requireNetworkConnection {
            viewModel.handleEvent(
                AddEditFarmEvent.OnSubmit
            )
        }
    }

    private fun showMapPreview(mapImageUri: Uri?) {
        binding.farmMapImageView.load(
            requireContext(),
            mapImageUri?.toString()
        )
    }

    private fun showMapDefaultPreview() {
        Glide.with(context)
            .load(R.drawable.farm_map_placeholder)
            .into(binding.farmMapImageView)
    }

    private fun openAreaMeasureMapScreen(measureMethod: AreaMeasureMethod) {
        findNavController().navigate(
            AddEditFarmFragmentDirections.actionAddEditPondFragmentToAreaMeasureMapFragment(
                measureMethod,
            ),
        )
    }

    private fun openAreaMeasureMethodScreen() {
        findNavController().navigate(
            AddEditFarmFragmentDirections.actionAddEditPondFragmentToAreaMeasureMethodFragment(),
        )
    }

    private fun onImageResult(imageUri: Uri) {
        viewModel.handleEvent(
            AddEditFarmEvent.OnFarmImageChanged(imageUri)
        )
    }
}