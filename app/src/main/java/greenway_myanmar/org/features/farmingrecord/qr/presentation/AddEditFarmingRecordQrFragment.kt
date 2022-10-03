package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.asString
import greenway_myanmar.org.common.presentation.extensions.showSoftInput
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.pickers.FarmPickerFragment
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class AddEditFarmingRecordQrFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrFragmentBinding>()

    private val viewModel: AddEditFarmingRecordQrViewModel by viewModels()

    private val qrActivityViewModel: FarmingRecordQrActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFragmentResultListeners()
        handleBackPress()
    }

    private fun setupFragmentResultListeners() {
        setFragmentResultListener(FarmPickerFragment.REQUEST_KEY_FARM) { requestKey, bundle ->
            val farm = bundle.getParcelable(FarmPickerFragment.EXTRA_FARM) as? UiFarm
            if (farm != null) {
                viewModel.handleEvent(
                    AddEditFarmingRecordQrEvent.FarmChanged(farm)
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddEditFarmingRecordQrFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        binding.toolbar.setNavigationOnClickListener {
            navigateUpOrFinish()
        }

        val viewPager = binding.viewPager
        viewPager.isUserInputEnabled = false

        val adapter = AddEditQrPagerAdapter(this)
        viewPager.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.screenTitle }
                    .distinctUntilChanged()
                    .collect {
                        binding.toolbarTitle.text = it.asString(requireContext())
                    }
            }
            launch {
                viewModel.uiState.map { it.totalPage }.distinctUntilChanged().collect {
                    binding.progressIndicator.max = it
                }
            }
            launch {
                viewModel.uiState.map { it.currentProgress }.distinctUntilChanged().collect {
                    binding.progressIndicator.progress = it
                }
            }
            launch {
                viewModel.uiState.map { it.showProgressIndicator }.distinctUntilChanged().collect {
                    binding.progressIndicator.isVisible = it
                }
            }
            launch {
                viewModel.uiState.map { it.currentPageIndex }.distinctUntilChanged().collect {
                    binding.viewPager.currentItem = it
                }
            }
            launch {
                viewModel.uiState.map { it.loading }
                    .distinctUntilChanged()
                    .collect { loading ->
                        binding.loadingIndicator.isVisible = loading
                    }
            }
            launch {
                viewModel.uiState.map { it.showOrderSuccess }
                    .distinctUntilChanged()
                    .collect { show ->
                        if (show) {
                            navigateToSuccessScreen()
                            viewModel.handleEvent(AddEditFarmingRecordQrEvent.OrderSuccessShown)
                        }
                    }
            }
            launch {
                viewModel.uiState.map { it.refreshQrList }
                    .distinctUntilChanged()
                    .collect { refresh ->
                        if (refresh) {
                            qrActivityViewModel.handleEvent(
                                FarmingRecordQrEvent.Refresh
                            )
                        }
                    }
            }
            launch {
                viewModel.uiState.map { it.dismissed }
                    .distinctUntilChanged()
                    .collect { dismissed ->
                        if (dismissed) {
                            findNavController().popBackStack(
                                R.id.farmingRecordQrHomeFragment,
                                false
                            )
                        }
                    }
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentPageIndex = viewModel.getCurrentPageIndex()
            if (currentPageIndex == 0) {
                navigateUpOrFinish()
            } else {
                viewModel.handleEvent(AddEditFarmingRecordQrEvent.PageChanged(currentPageIndex - 1))
            }
        }
    }

    private fun navigateUpOrFinish() {
        if (!findNavController(requireView()).navigateUp()) {
            requireActivity().finish()
        }
    }

    private fun navigateToSuccessScreen() {
        findNavController().navigate(
            AddEditFarmingRecordQrFragmentDirections.actionAddEditFarmingRecordQrFragmentToFarmingRecordQrOrderSuccessFragment(
                viewModel.getQuantity()
            )
        )
    }

}
