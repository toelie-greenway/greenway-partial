package greenway_myanmar.org.features.fishfarmrecord.presentation.production.productionrecordlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrProductionRecordListFragmentBinding
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductionRecordListFragment : Fragment(R.layout.ffr_production_record_list_fragment) {

    private val binding by viewBinding(FfrProductionRecordListFragmentBinding::bind)
    private val viewModel by viewModels<ProductionRecordListViewModel>()
    private lateinit var adapter: ProductionListAdapter
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupList()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            this.observeTotalPrice()
            this.observeProductions()
            this.observeLoadingState()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupList() {
        adapter = ProductionListAdapter()
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.list.adapter = adapter
    }

    private fun CoroutineScope.observeTotalPrice() = launch {
        viewModel.uiState.map { it.totalPrice }
            .distinctUntilChanged()
            .collect {
                binding.totalPriceTextView.setAmount(it)
            }
    }

    private fun CoroutineScope.observeProductions() = launch {
        viewModel.uiState.map { it.productionRecords }
            .distinctUntilChanged()
            .collect {
                adapter.submitList(it)
            }
    }

    private fun CoroutineScope.observeLoadingState() = launch {
        viewModel.uiState.map { it.loadingState }
            .distinctUntilChanged()
            .collect {
                binding.loadingStateView.bind(it)
            }
    }


}