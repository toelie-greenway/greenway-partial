package greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrOpeningSeasonFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmDetailFragmentDirections
import greenway_myanmar.org.features.fishfarmrecord.presentation.farm.farmdetail.FarmDetailViewModel
import greenway_myanmar.org.features.fishfarmrecord.presentation.openingseason.epoxycontroller.OpeningSeasonEpoxyController
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OpeningSeasonFragment : Fragment(R.layout.ffr_opening_season_fragment) {

    private val viewModel: OpeningSeasonViewModel by viewModels()
    private val parentViewModel: FarmDetailViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val binding by viewBinding(FfrOpeningSeasonFragmentBinding::bind)
    private val navController: NavController by lazy {
        findNavController()
    }
    private var epoxyController: OpeningSeasonEpoxyController by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {
        epoxyController = OpeningSeasonEpoxyController(
            onAddExpenseClick = {
                Toast.makeText(requireContext(), "//TODO: Add New Expense", Toast.LENGTH_SHORT)
                    .show()
            },
            onViewCategoryExpensesClick = { categoryId ->
                navigateToExpenseListScreen(categoryId)
            },
            onAddProductionClick = {

            },
            onViewProductionsClick = {

            },
            onCloseSeasonClick = {
                navigateToSeasonEndScreen()
            }
        )
        epoxyController.adapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.epoxyRecyclerView.scrollToPosition(0)
                }
            }
        })
        binding.epoxyRecyclerView.addItemDecoration(
            EpoxyItemSpacingDecorator(
                UIUtils.dpToPx(
                    requireContext(),
                    8
                )
            )
        )
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)
//        binding.recyclerView.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
//        categoryAdapter = OpeningSeasonCategoryAdapter()
//        productionAdapter = OpeningSeasonProductionAdapter()
//        adapter = ConcatAdapter(categoryAdapter, productionAdapter)
//        binding.recyclerView.adapter = adapter
    }

    private fun navigateToExpenseListScreen(categoryId: String) {
        val seasonId = parentViewModel.getSeasonId()
        if (seasonId.isEmpty()) {
            return
        }

        navController.navigate(
            FarmDetailFragmentDirections.actionFarmDetailFragmentToExpenseListFragment(
                seasonId = seasonId,
                categoryId = categoryId
            )
        )
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeItemsUiState()
            observeFarmUiStateFromParentViewModel()
        }
    }

    private fun CoroutineScope.observeFarmUiStateFromParentViewModel() = launch {
        parentViewModel.farmUiState
            .collect { farmUiState ->
                viewModel.handleEvent(OpeningSeasonEvent.OnFarmChanged(farmUiState))
            }
    }

    private fun CoroutineScope.observeItemsUiState() = launch {
        viewModel.categoryListUiState.collect { uiState ->
            when (uiState) {
                is LoadingState.Success -> {
                    epoxyController.setItems(uiState.data)
                }
                else -> {
                    Timber.d("UiState: $uiState")
                }
            }
            binding.loadingStateView.bind(uiState)
        }
    }

    private fun navigateToSeasonEndScreen() {
        navController.navigate(
            FarmDetailFragmentDirections.actionFarmDetailFragmentToSeasonEndFragment()
        )
    }
}