package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ScrollView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.presentation.extensions.hideSoftInput
import greenway_myanmar.org.common.presentation.extensions.setTextError
import greenway_myanmar.org.common.presentation.extensions.showIme
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrConfirmFragmentBinding
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
        setupQuantityEditText()
        setupClickListeners()
    }

    private fun setupQuantityEditText() {
        binding.quantityTextInputEditText.doAfterTextChanged {
            parentViewModel.handleEvent(
                AddEditFarmingRecordQrEvent.QuantityChanged(it?.toString().orEmpty())
            )
        }
    }

    private fun setupClickListeners() {
        binding.submitButton.setOnClickListener {
            parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.ConfirmOrder)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.farmActivities }
                    .distinctUntilChanged()
                    .collect {
                        binding.farmingInfoView.setActivities(it )
                    }
            }
            launch {
                parentViewModel.uiState.map { it.quantityError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.quantityTextInputLayout.setTextError(error)
                        if (error != null) {
                            binding.quantityTextInputEditText.requestFocus()
                            binding.scrollView.scrollTo(0, 0)
                            binding.quantityTextInputEditText.showIme()
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
        }
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrConfirmFragment()
    }
}
