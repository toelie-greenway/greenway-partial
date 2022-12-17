package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrOpeningSeasonFragmentBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason.epoxycontroller.OpeningSeasonEpoxyController
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OpeningSeasonFragment : Fragment(R.layout.ffr_opening_season_fragment) {

    private val viewModel: OpeningSeasonViewModel by viewModels()
    private var binding: FfrOpeningSeasonFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }
    private var epoxyController: OpeningSeasonEpoxyController by autoCleared()

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
        epoxyController = OpeningSeasonEpoxyController(
            onAddExpenseClick = {
                Toast.makeText(requireContext(), "//TODO: Add New Expense", Toast.LENGTH_SHORT)
                    .show()
            },
            onViewCategoryExpensesClick = {
                Toast.makeText(
                    requireContext(),
                    "//TODO: Show Expense Category Detail",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onAddProductionClick = {

            },
            onViewProductionsClick = {

            },
            onAddFcrClick = {

            },
            onViewFcrsClick = {

            },
            onCloseSeasonClick = {

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

    private fun observeViewModel() {

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.categories }
                    .distinctUntilChanged()
                    .collect {
                        epoxyController.setCategories(it)
                    }
            }
            launch {
                viewModel.uiState.map { it.isProducible }
                    .distinctUntilChanged()
                    .collect {
                        epoxyController.setShowProduction(it)
                    }
            }
            launch {
                viewModel.uiState.map { it.isFcrRecordable }
                    .distinctUntilChanged()
                    .collect {
                        epoxyController.setShowFcr(it)
                    }
            }
            launch {
                viewModel.uiState.map { it.isCloseableSeason }
                    .distinctUntilChanged()
                    .collect {
                        epoxyController.setShowCloseSeason(it)
                    }
            }

        }
    }

}