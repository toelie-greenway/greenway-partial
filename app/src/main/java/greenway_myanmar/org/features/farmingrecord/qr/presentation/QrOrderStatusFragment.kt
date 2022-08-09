package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.FarmingRecordQrOrderStatusFragmentBinding
import greenway_myanmar.org.databinding.FarmingRecordQrOrderSuccessFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.FarmingRecordQrOrderStatusAdapter
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QrOrderStatusFragment : Fragment() {

    private var binding by autoCleared<FarmingRecordQrOrderStatusFragmentBinding>()

    private var adapter by autoCleared<FarmingRecordQrOrderStatusAdapter>()

    private val viewModel by viewModels<QrOrderStatusViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FarmingRecordQrOrderStatusFragmentBinding.inflate(inflater, container, false).apply {

            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeViewModel()

        viewModel.setOrderId("123")
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {
        adapter = FarmingRecordQrOrderStatusAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.statuses }.distinctUntilChanged().collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}
