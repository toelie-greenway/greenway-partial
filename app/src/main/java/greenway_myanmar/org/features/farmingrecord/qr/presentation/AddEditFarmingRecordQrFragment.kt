package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class AddEditFarmingRecordQrFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrFragmentBinding>()

    private val viewModel: AddEditFarmingRecordQrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleBackPress()
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
        val viewPager = binding.viewPager
        viewPager.isUserInputEnabled = false

        val adapter = AddEditQrPagerAdapter(this)
        viewPager.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
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
                viewModel.uiState.map { it.currentPageIndex }.distinctUntilChanged().collect {
                    binding.viewPager.currentItem = it
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
}
