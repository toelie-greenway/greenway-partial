package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.common.presentation.extensions.showSnackbar
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFormFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState
import greenway_myanmar.org.ui.widget.GreenWayLargeSwitchOptionInputView
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditFarmingRecordQrFormFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrFormFragmentBinding>()

    private val parentViewModel: AddEditFarmingRecordQrViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

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
            GreenWayLargeDropdownTextInputView.ClickCallback<UiFarm> {
            override fun loadItems() {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.LoadFarms)
            }

            override fun onItemSelected(item: UiFarm) {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.FarmChanged(item))
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
        binding.phoneOptionInputView.setClickCallback(object :
            GreenWayLargeSwitchOptionInputView.ClickCallback {
            override fun onCheckChanged(buttonView: CompoundButton, selected: Boolean) {
                parentViewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.OptInShowPhoneChanged(
                        selected
                    )
                )

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
        })
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.farmList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.farmDropdownInputView.setData(
                            LoadingState.fromResource(resource),
                            parentViewModel.uiState.value.showFarmDropdown
                        )
                    }
            }
            launch {
                parentViewModel.uiState.map { it.seasonList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.seasonDropdownInputView.setData(
                            LoadingState.fromResource(resource),
                            parentViewModel.uiState.value.showSeasonDropdown
                        )
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

    companion object {
        fun newInstance() = AddEditFarmingRecordQrFormFragment()
    }
}
