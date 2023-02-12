package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrClosedSeasonsFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmDetailViewModel
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ClosedSeasonsFragment : Fragment(R.layout.ffr_closed_seasons_fragment) {

    private val viewModel: ClosedSeasonsViewModel by viewModels()
    private val parentViewModel: FarmDetailViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var binding: FfrClosedSeasonsFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }
    private var adapter: ClosedSeasonAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrClosedSeasonsFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {
        adapter = ClosedSeasonAdapter()
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.list.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeClosedSeasons()
            observeFarmIdFromParentViewModel()
            observeFarmUiStateFromParentViewModel()
        }
    }

    private fun CoroutineScope.observeClosedSeasons() = launch {
        viewModel.seasons.collect { uiState ->
            Timber.d("UiState: $uiState")
            if (uiState is LoadingState.Success) {
                adapter.submitList(uiState.data)
            }
            binding.loadingStateView.bind(uiState)
        }
    }

    private fun CoroutineScope.observeFarmUiStateFromParentViewModel() = launch {
        parentViewModel.farmUiState
            .collect { farmUiState ->
                viewModel.handleEvent(ClosedSeasonsEvent.OnFarmChanged(farmUiState))
            }
    }

    private fun CoroutineScope.observeFarmIdFromParentViewModel() = launch {
        viewModel.handleEvent(
            ClosedSeasonsEvent.OnFarmIdChanged(parentViewModel.getFarmId())
        )
    }

}