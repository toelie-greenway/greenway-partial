package greenway_myanmar.org.features.fishfarmrecord.presentation.closedseasons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrClosedSeasonsFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ClosedSeasonsFragment : Fragment(R.layout.ffr_closed_seasons_fragment) {

    private val viewModel: ClosedSeasonsViewModel by viewModels()
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
        binding.recyclerView.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.closedSeasons }
                    .distinctUntilChanged()
                    .collect {
                        adapter.submitList(it)
                    }
            }
        }
    }

}