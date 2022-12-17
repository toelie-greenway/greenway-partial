package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrOpeningSeasonFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant

@AndroidEntryPoint
class OpeningSeasonFragment : Fragment(R.layout.ffr_opening_season_fragment) {

    private val viewModel: OpeningSeasonViewModel by viewModels()
    private var binding: FfrOpeningSeasonFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }
    private var categoryAdapter: OpeningSeasonCategoryAdapter by autoCleared()
    private var productionAdapter: OpeningSeasonProductionAdapter by autoCleared()
    private var adapter: ConcatAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrOpeningSeasonFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {
        binding.recyclerView.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        categoryAdapter = OpeningSeasonCategoryAdapter()
        productionAdapter = OpeningSeasonProductionAdapter()
        adapter = ConcatAdapter(categoryAdapter, productionAdapter)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.categories }
                    .distinctUntilChanged()
                    .collect {
                        categoryAdapter.submitList(it)
                        productionAdapter.uiState =  OpeningSeasonProductionListItemUiState(
                                Instant.now(), BigDecimal.ONE
                        )
//                        binding.recyclerView.postDelayed(Runnable {
//                            binding.recyclerView.scrollToPosition(0)
//                        }, 700)
                    }
            }
        }
    }

}