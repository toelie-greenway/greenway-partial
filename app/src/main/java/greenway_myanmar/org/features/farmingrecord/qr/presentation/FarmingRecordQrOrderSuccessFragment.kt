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
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.databinding.FarmingRecordQrOrderSuccessFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmingRecordQrOrderSuccessFragment : Fragment() {

    private var binding: FarmingRecordQrOrderSuccessFragmentBinding by autoCleared()

    private val viewModel: FarmingRecordQrOrderSuccessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FarmingRecordQrOrderSuccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        binding.viewOrderStatusButton.setOnClickListener {
            navigateToOrderStatusScreen()
        }
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack(R.id.farmingRecordQrHomeFragment, false)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.message }
                    .distinctUntilChanged()
                    .collect { message ->
                        binding.messageTextView.text = message.asString(requireContext())
                    }
            }
        }
    }

    private fun navigateToOrderStatusScreen() {
        findNavController().popBackStack(R.id.farmingRecordQrHomeFragment, false)
    }
}
