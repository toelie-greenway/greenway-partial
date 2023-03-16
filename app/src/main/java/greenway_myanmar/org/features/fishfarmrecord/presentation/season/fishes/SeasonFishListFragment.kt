package greenway_myanmar.org.features.fishfarmrecord.presentation.season.fishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.presentation.decorators.SpacingItemDecoration
import greenway_myanmar.org.databinding.FfrSeasonFishListFragmentBinding
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonFishListFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FfrSeasonFishListFragmentBinding::inflate)

    private val viewModel by viewModels<SeasonFishListViewModel>()

    private val adapter by lazy {
        SeasonFishListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {
        binding.list.addItemDecoration(
            SpacingItemDecoration(
                requireContext(), 0, 0, 0, 8
            )
        )
        binding.list.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFishes()
        }
    }

    private fun CoroutineScope.observeFishes() = launch {
        viewModel.uiState.map { it.fishes }
            .distinctUntilChanged()
            .collect {
                adapter.submitList(it)
            }
    }
}