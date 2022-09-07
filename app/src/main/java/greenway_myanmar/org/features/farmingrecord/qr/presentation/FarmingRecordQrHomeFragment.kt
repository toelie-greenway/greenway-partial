package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.common.presentation.decorators.SpacingItemDecoration
import greenway_myanmar.org.databinding.FarmingRecordQrHomeFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.QrOrderListAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrOrder
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FarmingRecordQrHomeFragment : Fragment() {

    private var binding by autoCleared<FarmingRecordQrHomeFragmentBinding>()

    private var adapter by autoCleared<QrOrderListAdapter>()

    private val viewModel by viewModels<FarmingRecordQrHomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FarmingRecordQrHomeFragmentBinding.inflate(inflater, container, false).apply {
            newQrOrderButton.setOnClickListener {
                navigateToAddEditQrScreen()
            }
        }
        return binding.root
    }

    private fun navigateToAddEditQrScreen() {
        findNavController().navigate(
            FarmingRecordQrHomeFragmentDirections.actionFarmingRecordQrHomeFragmentToAddEditFarmingRecordQrFragment()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(greenway_myanmar.org.R.menu.farming_record_qr_home)
            setNavigationOnClickListener {
                Toast.makeText(requireContext(), "//TODO: back or close", Toast.LENGTH_SHORT).show()
            }
            setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == greenway_myanmar.org.R.id.action_support) {
                    Toast.makeText(
                        requireContext(),
                        "//TODO: navigate to support!",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupUi() {
        setupRetryButton()
        setupList()
    }

    private fun setupRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.handleEvent(FarmingRecordQrHomeEvent.Retry)
        }
    }

    private fun setupList() {
        val itemClickCallback = object : QrOrderListAdapter.ItemClickCallback {
            override fun onQrClick(qrUrl: String) {
                navigateToQrFullScreen(qrUrl)
            }

            override fun onItemClick(item: UiQrOrder) {
                navigateToOrderDetailScreen(item)
            }
        }
        adapter = QrOrderListAdapter(requireContext(), itemClickCallback)
        binding.qrOrderList.adapter = adapter
        binding.qrOrderList.apply {
            addItemDecoration(SpacingItemDecoration(requireContext(), 0, 0, 0, 16))
        }
    }

    private fun createDividerDrawable(): InsetDrawable {
        val attrs = intArrayOf(android.R.attr.listDivider)
        val a = requireContext().obtainStyledAttributes(attrs)
        val divider = a.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.spacing_16)
        val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
        a.recycle()
        return insetDivider
    }

    private fun navigateToQrFullScreen(qrData: String) {
        findNavController().navigate(
            FarmingRecordQrHomeFragmentDirections
                .actionFarmingRecordQrHomeFragmentToQrFullScreenFragment(qrData)
        )
    }

    private fun navigateToOrderDetailScreen(order: UiQrOrder) {
//        findNavController().navigate(
//            FarmingRecordQrHomeFragmentDirections
//                .actionFarmingRecordQrHomeFragmentToQrFullScreenFragment(qrData)
//        )
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.qrOrderList }
                    .distinctUntilChanged()
                    .collect { list ->
                        adapter.submitList(list)
                        binding.qrOrderList.isVisible = list.isNotEmpty()
                    }
            }
            launch {
                viewModel.uiState.map { it.qrOrderListError }
                    .distinctUntilChanged()
                    .collect { error ->
                        binding.errorMessage.text = error?.asString(requireContext())
                        binding.errorMessage.isVisible = error != null
                        binding.retryButton.isVisible = error != null
                    }
            }
            launch {
                viewModel.uiState.map { it.qrOrderListLoading }
                    .distinctUntilChanged()
                    .collect { loading ->
                        binding.loadingIndicator.isVisible = loading
                    }
            }
        }
    }

}
