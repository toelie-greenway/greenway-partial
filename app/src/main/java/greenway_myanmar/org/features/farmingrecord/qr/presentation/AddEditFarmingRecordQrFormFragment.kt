package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFormFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.dialogs.PhoneInputDialog
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrLifetime
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrQuantity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason
import greenway_myanmar.org.ui.widget.*
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant

@AndroidEntryPoint
class AddEditFarmingRecordQrFormFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrFormFragmentBinding>()

    private val parentViewModel: AddEditFarmingRecordQrViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(PhoneInputDialog.REQUEST_KEY_PHONE) { _, bundle ->
            val phone = bundle.getString(PhoneInputDialog.EXTRA_PHONE).orEmpty()
            parentViewModel.handleEvent(
                AddEditFarmingRecordQrEvent.PhoneChanged(phone)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            AddEditFarmingRecordQrFormFragmentBinding.inflate(inflater, container, false).apply {
                submitButton.setOnClickListener {
                    parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.Submit)
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        binding.farmDropdownInputView.setClickCallback(object :
            GreenWayLargePickerTextInputView.ClickCallback {
            override fun onClick() {
                showFarmListPicker()
            }
        })
        binding.seasonDropdownInputView.setClickCallback(object :
            GreenWayLargeDropdownTextInputView.ClickCallback<UiSeason> {
            override fun loadItems() {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.LoadSeasons)
            }

            override fun onItemSelected(item: UiSeason) {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.SeasonChanged(item))
            }
        })
        binding.farmLocationTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            parentViewModel.handleEvent(
                AddEditFarmingRecordQrEvent.FarmLocationTypeChanged(
                    getFarmLocationTypeFromViewId(checkedId)
                )
            )
        }
        binding.phoneInputView.setClickCallback(object :
            GreenWayLargeSwitchPhoneInputView.ClickCallback {
            override fun onPhoneChanged(phone: String) {
                parentViewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.PhoneChanged(phone)
                )
            }

            override fun onCheckChanged(buttonView: CompoundButton, selected: Boolean) {
                parentViewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.OptInShowPhoneChanged(
                        selected
                    )
                )
            }

            override fun onItemClick() {
                // no-op
            }

        })
        binding.farmInputOptionInputView.setClickCallback(object :
            GreenWayLargeSwitchOptionInputView.ClickCallback {
            override fun onCheckChanged(buttonView: CompoundButton, selected: Boolean) {
                parentViewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.OptInShowFarmInputChanged(
                        selected
                    )
                )
            }

            override fun onItemClick() {
                // no-op
            }
        })
        binding.yieldInputOptionInputView.setClickCallback(object :
            GreenWayLargeSwitchOptionInputView.ClickCallback {
            override fun onCheckChanged(buttonView: CompoundButton, selected: Boolean) {
                parentViewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.OptInShowYieldChanged(
                        selected
                    )
                )
            }

            override fun onItemClick() {
                // no-op
            }
        })
        setupQrLifetimeInput()
    }

    private fun setupQrLifetimeInput() {
        binding.qrLifetimeDropdownInputView.setClickCallback(object :
            GreenWayLargeDropdownTextInputView.ClickCallback<UiQrLifetime> {
            override fun loadItems() {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.LoadQrLifetimes)
            }

            override fun onItemSelected(item: UiQrLifetime) {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.QrLifetimeChanged(item))
            }
        })
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.farm }
                    .distinctUntilChanged()
                    .collect { farm ->
                        if (farm == null) {
                            binding.farmDropdownInputView.removeSelection()
                        } else {
                            binding.farmDropdownInputView.setSelection(farm)
                        }
                    }
            }
            launch {
                parentViewModel.uiState.map { it.seasonList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.seasonDropdownInputView.setData(
                            LoadingState.fromResource(resource),
                            parentViewModel.shouldExpandSeasonDropdown()
                        )
                        if (resource?.isSuccess() == true) {
                            parentViewModel.handleEvent(
                                AddEditFarmingRecordQrEvent.SeasonDropdownShown
                            )
                        }
                    }
            }
            launch {
                parentViewModel.uiState.map { it.season }
                    .distinctUntilChanged()
                    .collect { season ->
                        if (season == null) {
                            binding.seasonDropdownInputView.removeSelection()
                        }
                    }
            }
            launch {
                parentViewModel.uiState.map { it.farmError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.farmDropdownInputView.setError(error)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.seasonError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.seasonDropdownInputView.setError(error)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.farmLocationTypeError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.farmLocationTypeErrorTextView.text =
                            error?.asString(requireContext()).orEmpty()
                        binding.farmLocationTypeErrorTextView.isVisible = error != null
                    }
            }
            launch {
                parentViewModel.uiState.map { it.phoneError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.phoneInputView.setError(error)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.qrLifetimeError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.qrLifetimeDropdownInputView.setError(error)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.qrLifetimeList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.qrLifetimeDropdownInputView.setData(
                            LoadingState.fromResource(
                                resource
                            ),
                            parentViewModel.uiState.value.showQuantityDropdown
                        )
                    }
            }
            launch {
                parentViewModel.uiState.map { it.createQrError }
                    .distinctUntilChanged()
                    .collect { error ->
                        if (error != null) {
                            showSnackbar(error, false)
                            parentViewModel.handleEvent(
                                AddEditFarmingRecordQrEvent.CreateQrErrorShown
                            )
                        }
                    }
            }
            launch {
                parentViewModel.uiState.map { it.loading }
                    .distinctUntilChanged()
                    .collect { loading ->
                        binding.submitButton.isVisible = !loading
                    }
            }
            launch {
                parentViewModel.uiState.map { it.farmLocationType }
                    .distinctUntilChanged()
                    .collect { type ->
                        updateFarmLocationTypeUi(type)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.showPhoneInput }
                    .distinctUntilChanged()
                    .collect { show ->
                        if (show) {
                            showPhoneInputDialog()
                            markPhoneInputShown()
                        }
                    }
            }
            launch {
                parentViewModel.uiState.map { it.phone }
                    .distinctUntilChanged()
                    .collect { phone ->
                        //binding.phon
                    }
            }
        }
    }

    private fun updateFarmLocationTypeUi(type: UiFarmLocationType?) {
        val checkedId = binding.farmLocationTypeRadioGroup.checkedRadioButtonId
        val newCheckedId = getViewIdFromFarmLocationType(type)

        if (checkedId != newCheckedId) {
            binding.farmLocationTypeRadioGroup.check(newCheckedId)
        }
    }

    private fun getFarmLocationTypeFromViewId(@IdRes radioButtonId: Int): UiFarmLocationType? {
        return when (radioButtonId) {
            R.id.farm_location_map_radio_button -> {
                UiFarmLocationType.Map
            }
            R.id.farm_location_township_radio_button -> {
                UiFarmLocationType.Township
            }
            R.id.farm_location_village_radio_button -> {
                UiFarmLocationType.Village
            }
            else -> {
                null
            }
        }
    }

    @IdRes
    private fun getViewIdFromFarmLocationType(type: UiFarmLocationType?): Int {
        if (type == null) {
            return -1
        }

        return when (type) {
            UiFarmLocationType.Map -> {
                R.id.farm_location_map_radio_button
            }
            UiFarmLocationType.Township -> {
                R.id.farm_location_township_radio_button
            }
            UiFarmLocationType.Village -> {
                R.id.farm_location_village_radio_button
            }
        }
    }

    private fun showFarmListPicker() {
        val direction =
            AddEditFarmingRecordQrFragmentDirections.actionAddEditFarmingRecordQrFragmentToFarmPickerDialogFragment()
        findNavController()
            .navigate(direction)
    }

    private fun showPhoneInputDialog() {
        PhoneInputDialog.newInstance()
            .show(childFragmentManager, "phone-input")
    }

    private fun markPhoneInputShown() {
        parentViewModel.handleEvent(
            AddEditFarmingRecordQrEvent.PhoneInputShown
        )
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrFormFragment()
    }
}
