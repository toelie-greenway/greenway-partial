package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import android.graphics.Color
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.FfrAddEditSeasonFragmentBinding
import greenway_myanmar.org.features.areameasure.domain.model.AreaMeasureMethod
import greenway_myanmar.org.features.areameasure.presentation.AreaMeasureMapFragment
import greenway_myanmar.org.features.areameasure.presentation.AreaMeasureMethodFragment
import greenway_myanmar.org.features.areameasure.presentation.model.AreaMeasurement
import greenway_myanmar.org.features.areameasure.presentation.model.toStaticMapUrl
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.loanduration.CustomLoadDurationInputBottomSheetFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.views.FishListInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.views.FishListInputView.OnItemClickListener
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput.FishInputFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiLoanDuration
import greenway_myanmar.org.ui.widget.GreenWayDateInputView.OnDateChangeListener
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getNavigationResult
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.load
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

@AndroidEntryPoint
class AddEditSeasonFragment : Fragment(R.layout.ffr_add_edit_season_fragment) {

    private val viewModel: AddEditSeasonViewModel by viewModels()
    private val binding by viewBinding(FfrAddEditSeasonFragmentBinding::bind)
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
        setupToolbar()
        setupUi()
        observeViewModel()
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
            getString(R.string.ffr_transition_name_screen_add_edit_season)
        )
    }

    private fun setupFragmentResultListener() {
        setAreaMeasureResultListener()
        setFishInputFragmentResultListener()
        setCustomLoanDurationResultListener()
    }

    private fun setupUi() {
        setupFarmMeasurementInputUi()
        setupSeasonNameInputUi()
        setupSeasonStartDateInputUi()
        setupFishListInputView()
        setupCompanyCodeInputUi()
        setupLoanInformationInputUi()
        setupSubmitButton()
        setupCancelButton()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFarmMeasurementLoadingState()

            observeEditFarmMeasurementCheck()
            observeFarmArea()
            observeFarmAreaError()
            observeFarmMeasurement()

            observeSeasonName()
            observeSeasonNameError()

            observeSeasonStartDate()

            observeFishes()
            observeFishesError()

            observeCompanyCode()
            observeCompanyCodeError()
            observeCompany()
            observeCompanyLoadingState()

            observeInputLoanCheck()
            observeLoanAmount()
            observeLoanDuration()
            observeLoanOrganization()
            observeLoanRemark()

            observeLoanAmountError()
            observeLoanDurationError()
            observeLoanOrganizationError()
        }
    }


    private fun setAreaMeasureResultListener() {
        getNavigationResult<AreaMeasureMethod>(
            R.id.addEditSeasonFragment,
            AreaMeasureMethodFragment.RESULT_KEY_MEASURE_METHOD
        ) { result ->
            openAreaMeasureMapScreen(result)
        }
        getNavigationResult<AreaMeasurement>(
            R.id.addEditSeasonFragment,
            AreaMeasureMapFragment.RESULT_KEY_MEASURE_MAP
        ) { result ->
            onAreaMeasureResult(result)
        }
    }

    private fun onAreaMeasureResult(result: AreaMeasurement) {
        viewModel.handleEvent(AddEditSeasonEvent.OnFarmAreaMeasurementChanged(result))
    }


    private fun setFishInputFragmentResultListener() {
        setFragmentResultListener(FishInputFragment.REQUEST_KEY_FISH) { _, bundle ->
            val fish = bundle.getParcelableExtraCompat<UiFish>(FishInputFragment.KEY_FISH)
            if (fish != null) {
                viewModel.handleEvent(
                    AddEditSeasonEvent.OnFishAdded(fish)
                )
            }
        }
    }

    private fun setCustomLoanDurationResultListener() {
        getNavigationResult<Int>(
            R.id.addEditSeasonFragment,
            CustomLoadDurationInputBottomSheetFragment.KEY_LOAN_DURATION_MONTH
        ) {
            onCustomLoanDurationResult(it)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.ffrb_add_edit_season)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_done) {
                onSubmit()
                true
            } else {
                false
            }
        }
    }

    private fun setupFarmMeasurementInputUi() {
        setupEditFarmMeasurementCheckBox()
        setupFarmLocationInputUi()
        setupFarmAreaInputUi()
    }

    private fun setupEditFarmMeasurementCheckBox() {
        binding.editFarmMeasurementCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.handleEvent(AddEditSeasonEvent.OnEditFarmMeasurementCheckChanged(isChecked))
        }
    }


    private fun setupFarmLocationInputUi() {
        binding.farmMapImageView.setOnClickListener {
            openAreaMeasureMethodScreen()
        }
    }

    private fun setupFarmAreaInputUi() {
        binding.farmAreaTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnFarmAreaChanged(it?.toString()))
        }
    }

    private fun setupSeasonNameInputUi() {
        binding.seasonNameTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnSeasonNameChanged(it?.toString()))
        }
    }

    private fun setupSeasonStartDateInputUi() {
        binding.seasonStartDateInputView.onDateChangeListener = object : OnDateChangeListener {
            override fun onDateChanged(date: LocalDate) {
                viewModel.handleEvent(AddEditSeasonEvent.OnSeasonStartDateChanged(date))
            }
        }
    }

    private fun setupFishListInputView() {
        binding.fishListInputView.setOnDataSetChangeListener(object :
            FishListInputView.OnDataSetChangeListener {
            override fun onDataSetChanged(items: List<UiFish>) {
                viewModel.handleEvent(AddEditSeasonEvent.OnFishListChanged(items))
            }
        })
        binding.fishListInputView.setOnItemClickListener(object : OnItemClickListener {
            override fun onAddNewFishClick() {
                openFishInputScreen()
            }

            override fun onFishItemClick() {
            }
        })
    }

    private fun setupCompanyCodeInputUi() {
        binding.companyCodeTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnCompanyCodeChanged(it?.toString()))
        }
    }

    private fun setupLoanInformationInputUi() {
        setupInputLoanInformationCheckBox()
        setupLoanAmountInputUi()
        setupLoanDurationInputUi()
        setupLoanOrganizationInputUi()
        setupLoanRemarkInputUi()
    }

    private fun setupInputLoanInformationCheckBox() {
        binding.inputLoanCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.handleEvent(AddEditSeasonEvent.OnInputLoanInformationCheckChanged(isChecked))
        }
    }

    private fun setupLoanAmountInputUi() {
        binding.loanTotalAmountTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnLoanAmountChanged(it?.toString()))
        }
    }

    private fun setupLoanDurationInputUi() {
        binding.loanDuration3MonthsButton.setOnClickListener { button ->
            updateResetLoanDuration(UiLoanDuration.ThreeMonth, (button as MaterialButton).isChecked)
        }
        binding.loanDuration6MonthsButton.setOnClickListener { button ->
            updateResetLoanDuration(UiLoanDuration.SixMonth, (button as MaterialButton).isChecked)

        }
        binding.loanDuration12MonthsButton.setOnClickListener { button ->
            updateResetLoanDuration(
                UiLoanDuration.TwelveMonth,
                (button as MaterialButton).isChecked
            )

        }
        binding.loanDurationCustomButton.setOnClickListener { button ->
            (button as MaterialButton).isChecked = false
            // resetLoanDuration()
            openCustomLoadDurationInputScreen()
        }
    }

    private fun updateResetLoanDuration(duration: UiLoanDuration?, isChecked: Boolean) {
        if (isChecked) {
            updateLoanDuration(duration)
        } else {
            resetLoanDuration()
        }
    }

    private fun updateLoanDuration(duration: UiLoanDuration?) {
        viewModel.handleEvent(AddEditSeasonEvent.OnLoanDurationChanged(duration))
    }

    private fun resetLoanDuration() {
        viewModel.handleEvent(AddEditSeasonEvent.OnLoanDurationChanged(null))
    }

    private fun setupLoanOrganizationInputUi() {
        binding.loanOrganizationTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnLoanOrganizationChanged(it?.toString()))
        }
    }

    private fun setupLoanRemarkInputUi() {
        binding.loanRemarkTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(AddEditSeasonEvent.OnLoanRemarkChanged(it?.toString()))
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun CoroutineScope.observeEditFarmMeasurementCheck() = launch {
        viewModel.uiState.map { it.editFarmMeasurement }
            .distinctUntilChanged()
            .collect { newValue ->
                val oldValue = binding.editFarmMeasurementCheckBox.isChecked
                if (oldValue != newValue) {
                    binding.editFarmMeasurementCheckBox.isChecked = newValue
                }
                binding.farmMeasurementContainer.isVisible = newValue
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

    private fun CoroutineScope.observeFarmMeasurementLoadingState() = launch {
        launch {
            viewModel.farmMeasurementUiState
                .collect { uiState ->
                    binding.farmMeasurementLoadingContainer.isVisible = uiState is LoadingState.Loading
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

    private fun CoroutineScope.observeSeasonName() = launch {
        viewModel.uiState.map { it.seasonName }
            .distinctUntilChanged()
            .collect {
                binding.seasonNameTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeSeasonNameError() = launch {
        viewModel.uiState.map { it.seasonNameError }
            .distinctUntilChanged()
            .collect {
                binding.seasonNameTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeSeasonStartDate() = launch {
        viewModel.uiState.map { it.seasonStartDate }
            .distinctUntilChanged()
            .collect {
                binding.seasonStartDateInputView.setDate(it)
            }
    }

    private fun CoroutineScope.observeFishes() = launch {
        viewModel.uiState.map { it.fishes }
            .distinctUntilChanged()
            .collect {
                binding.fishListInputView.setItems(it)
            }
    }

    private fun CoroutineScope.observeFishesError() = launch {
        viewModel.uiState.map { it.fishesError }
            .distinctUntilChanged()
            .collect {
                binding.fishListInputView.setError(it)
            }
    }

    private fun CoroutineScope.observeCompanyCode() = launch {
        viewModel.uiState.map { it.companyCode }
            .distinctUntilChanged()
            .collect {
                binding.companyCodeTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeCompanyCodeError() = launch {
        viewModel.uiState.map { it.companyCodeError }
            .distinctUntilChanged()
            .collect {
                binding.companyCodeTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeCompanyLoadingState() = launch {
        viewModel.companyUiState
            .collect { companyUiState ->
                binding.companyCodeTextInputLayout.isEnabled =
                    companyUiState !is LoadingState.Loading
            }
    }

    private fun CoroutineScope.observeCompany() = launch {
        viewModel.uiState.map { it.company }
            .distinctUntilChanged()
            .collect {
                binding.companyCodeCorrectImageView.isVisible = it != null
                binding.companyNameTextView.text = it?.name.orEmpty()
                binding.companyNameTextView.isVisible = it != null
            }
    }

    private fun CoroutineScope.observeInputLoanCheck() = launch {
        viewModel.uiState.map { it.inputLoanInformation }
            .distinctUntilChanged()
            .collect { newValue ->
                val oldValue = binding.inputLoanCheckBox.isChecked
                if (oldValue != newValue) {
                    binding.inputLoanCheckBox.isChecked = newValue
                }
                binding.loanInformationInputContainer.isVisible = newValue
            }
    }

    private fun CoroutineScope.observeLoanAmount() = launch {
        viewModel.uiState.map { it.loanAmount }
            .distinctUntilChanged()
            .collect {
                binding.loanTotalAmountTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeLoanDuration() = launch {
        viewModel.uiState.map { it.loanDuration }
            .distinctUntilChanged()
            .collect { duration ->
                Timber.d("Loan Duration: $duration")
                binding.loanDuration3MonthsButton.isChecked = duration is UiLoanDuration.ThreeMonth
                binding.loanDuration6MonthsButton.isChecked = duration is UiLoanDuration.SixMonth
                binding.loanDuration12MonthsButton.isChecked =
                    duration is UiLoanDuration.TwelveMonth
                binding.loanDurationCustomButton.isChecked = duration is UiLoanDuration.Custom

                if (duration is UiLoanDuration.Custom) {
                    binding.loanDurationCustomButton.text =
                        getString(
                            R.string.ffr_add_edit_season_label_loan_duration_custom_months,
                            duration.month
                        )
                } else {
                    binding.loanDurationCustomButton.text =
                        getString(
                            R.string.ffr_add_edit_season_label_loan_duration_custom
                        )
                }
            }
    }

    private fun CoroutineScope.observeLoanOrganization() = launch {
        viewModel.uiState.map { it.loanOrganization }
            .distinctUntilChanged()
            .collect {
                binding.loanOrganizationTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeLoanRemark() = launch {
        viewModel.uiState.map { it.loanRemark }
            .distinctUntilChanged()
            .collect {
                binding.loanRemarkTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeLoanAmountError() = launch {
        viewModel.uiState.map { it.loanAmountError }
            .distinctUntilChanged()
            .collect {
                binding.loanTotalAmountTextInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeLoanDurationError() = launch {
        viewModel.uiState.map { it.loanDurationError }
            .distinctUntilChanged()
            .collect { error ->
                binding.loanDurationErrorTextView.isVisible = error != null
                binding.loanDurationErrorTextView.text = error?.asString(requireContext()).orEmpty()
            }
    }

    private fun CoroutineScope.observeLoanOrganizationError() = launch {
        viewModel.uiState.map { it.loanOrganizationError }
            .distinctUntilChanged()
            .collect {
                binding.loanOrganizationTextInputLayout.setError(it)
            }
    }

    private fun onCustomLoanDurationResult(month: Int) {
        updateLoanDuration(UiLoanDuration.Custom(month))
    }

    private fun onSubmit() {
        viewModel.handleEvent(AddEditSeasonEvent.OnSubmit)
    }

    private fun openFishInputScreen() {
        navController.navigate(
            AddEditSeasonFragmentDirections.actionAddEditSeasonFragmentToFishInputFragment()
        )
    }

    private fun openCustomLoadDurationInputScreen() {
        val month = (viewModel.currentUiState.loanDuration as? UiLoanDuration.Custom)?.month ?: 0
        navController.navigate(
            AddEditSeasonFragmentDirections
                .actionAddEditSeasonFragmentToCustomLoadDurationInputBottomSheetFragment(month)
        )
    }

    private fun openAreaMeasureMethodScreen() {
        findNavController().navigate(
            AddEditSeasonFragmentDirections.actionAddEditSeasonFragmentToAreaMeasureMethodFragment()
        )
    }

    private fun openAreaMeasureMapScreen(measureMethod: AreaMeasureMethod) {
        findNavController().navigate(
            AddEditSeasonFragmentDirections.actionAddEditSeasonFragmentToAreaMeasureMapFragment(
                measureMethod,
            )
        )
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
}