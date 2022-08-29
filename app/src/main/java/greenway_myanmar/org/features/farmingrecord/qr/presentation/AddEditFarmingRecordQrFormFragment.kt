package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFormFragmentBinding
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState
import greenway_myanmar.org.ui.widget.GreenWayLargeDropdownTextInputView.LoadingState.Companion
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.vo.Resource
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
                    parentViewModel.handleEvent(
                        AddEditFarmingRecordQrEvent.PageChanged(
                            AddEditQrPagerAdapter.CONFIRM_PAGE_INDEX
                        )
                    )
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.qrNumber }
                    .distinctUntilChanged()
                    .collect {
                        updateQrNumber(it)
                    }
            }
            launch {
                parentViewModel.uiState.map { it.farmList }
                    .distinctUntilChanged()
                    .collect { resource ->
                        binding.farmDropdownInputView.setData(LoadingState.fromResource(resource))
                    }
            }
        }
    }

    private fun updateQrNumber(newValue: String) {
        val oldValue = binding.qrNumberTextView.text.toString()
        if (oldValue != newValue) {
            binding.qrNumberTextView.text = newValue
        }
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrFormFragment()
    }
}
