package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrConfirmFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrQuantity
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditFarmingRecordQrConfirmFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrConfirmFragmentBinding>()

    private val parentViewModel: AddEditFarmingRecordQrViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val viewModel: AddEditFarmingRecordQrConfirmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            AddEditFarmingRecordQrConfirmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupQuantityInput()
        setupClickListeners()
    }

    private fun setupQuantityInput() {
        binding.quantityDropdownInputView.setClickCallback(object :
            GreenWayLargeDropdownTextInputView.ClickCallback<UiQrQuantity> {
            override fun loadItems() {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.LoadQuantities)
            }

            override fun onItemSelected(item: UiQrQuantity) {
                parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.QuantityChanged(item))
            }
        })
    }

    private fun setupClickListeners() {
        binding.submitButton.setOnClickListener {
            parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.ConfirmOrder)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.qrDetail }
                    .distinctUntilChanged()
                    .collect {
                        binding.qrDetailView.bind(it)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.quantityError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.quantityDropdownInputView.setError(error)
                        if (error != null) {
                            binding.scrollView.scrollTo(0, 0)
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
                parentViewModel.uiState.map { it.quantityList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.quantityDropdownInputView.setData(
                            GreenWayLargeDropdownTextInputView.LoadingState.fromResource(
                                resource
                            ),
                            parentViewModel.uiState.value.showQuantityDropdown
                        )
                    }
            }
            launch {
                parentViewModel.uiState.map { it.estimatedPriceLabel }
                    .distinctUntilChanged()
                    .collect { text ->
                        binding.estimatedPriceTextView.text =
                            text?.asString(requireContext()).orEmpty()
                        binding.estimatedPriceTextView.isVisible = text != null
                    }
            }

            launch {
                parentViewModel.uiState.map { it.showQrDetail }
                    .distinctUntilChanged()
                    .collect { show ->
                        binding.qrDetailView.isVisible = show
                    }
            }
        }
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrConfirmFragment()
    }
}
