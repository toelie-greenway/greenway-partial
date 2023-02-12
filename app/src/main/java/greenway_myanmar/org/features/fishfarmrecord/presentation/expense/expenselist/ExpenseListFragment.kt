package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.expenselist

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
import greenway_myanmar.org.databinding.FfrExpenseListFragmentBinding
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseListFragment : Fragment(R.layout.ffr_expense_list_fragment) {

    private val binding by viewBinding(FfrExpenseListFragmentBinding::bind)
    private val viewModel by viewModels<ExpenseListViewModel>()
    private lateinit var adapter: ExpenseListAdapter
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
            this.observeCategory()
            this.observeTotalExpense()
            this.observeExpenses()
            this.observeLoadingState()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupList() {
        adapter = ExpenseListAdapter()
        binding.list.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.list.adapter = adapter
    }

    private fun CoroutineScope.observeCategory() = launch {
        viewModel.uiState.map { it.category }
            .distinctUntilChanged()
            .collect {
                binding.toolbarTitleTextView.text = it?.name.orEmpty()
            }
    }

    private fun CoroutineScope.observeTotalExpense() = launch {
        viewModel.uiState.map { it.total }
            .distinctUntilChanged()
            .collect {
                binding.totalExpenseTextView.setAmount(it)
            }
    }

    private fun CoroutineScope.observeExpenses() = launch {
        viewModel.uiState.map { it.expenses }
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