package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrbHomeFragmentBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.domain.model.ContractFarmingCompany
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FishFarmerRecordBookHomeFragment : Fragment(R.layout.ffrb_home_fragment) {

    private val viewModel: FishFarmerRecordBookHomeViewModel by viewModels()
    private var binding: FfrbHomeFragmentBinding by autoCleared()
    private var adapter: PondListAdapter by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbHomeFragmentBinding.bind(view)

        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(REQUEST_KEY_ADD_NEW_POND) { _, bundle ->
            val newPondCreated = bundle.getBoolean(BUNDLE_KEY_NEW_POND_CREATED)
            val pondId = bundle.getString(BUNDLE_KEY_POND_ID)
            if (newPondCreated && !pondId.isNullOrEmpty()) {
                openAddEditFarmingSeasonScreen(pondId)
            }
        }
    }

    private fun setupUi() {
        setupPondListUi()
    }

    private fun setupPondListUi() {
        adapter = PondListAdapter(
            onItemClick = {
                openPondDetailScreen(it)
            },
            onCompanyClick = {
                openCompanyScreen(it)
            },
            onAddNewSeasonClick = {
                openAddEditFarmingSeasonScreen(it.id)
            },
            onAddNewExpenseClick = {
                openAddEditExpenseScreen(it)
            }
        )
        binding.pondList.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.pondList.adapter = adapter
    }

    private fun openCompanyScreen(company: ContractFarmingCompany) {
        Toast.makeText(requireContext(), "TODO:// Open company screen", Toast.LENGTH_SHORT).show()
    }

    private fun openAddEditFishPondScreen() {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditPondFragment()
        )
    }

    private fun openAddEditFarmingSeasonScreen(pondId: String) {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditSeasonFragment()
        )
    }

    private fun openAddEditExpenseScreen(item: PondListItemUiState) {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditExpenseFragment()
        )
    }

    private fun openPondDetailScreen(pondListItemUiState: PondListItemUiState) {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToPondDetailFragment()
        )
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.ponds }
                    .distinctUntilChanged()
                    .collect {
                        adapter.submitList(it)
                    }
            }
        }
    }

    companion object {
        const val REQUEST_KEY_ADD_NEW_POND = "request_key.ADD_NEW_POND"
        const val BUNDLE_KEY_NEW_POND_CREATED = "bundle_key.NEW_POND_CREATED"
        const val BUNDLE_KEY_POND_ID = "bundle_key.POND_ID"
    }


}