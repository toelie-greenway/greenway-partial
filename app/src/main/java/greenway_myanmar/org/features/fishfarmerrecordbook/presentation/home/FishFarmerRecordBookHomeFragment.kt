package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbHomeFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FishFarmerRecordBookHomeFragment : Fragment(R.layout.ffrb_home_fragment) {

    private val viewModel: FishFarmerRecordBookHomeViewModel by viewModels()
    private var binding: FfrbHomeFragmentBinding by autoCleared()
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
                openAddEditFarmingSeasonScreen()
            }
        }
    }

    private fun setupUi() {
        binding.addNewFishPondButton.setOnClickListener {
            openAddEditFishPondScreen()
        }
        binding.addFarmingSeasonButton.setOnClickListener {
            openAddEditFarmingSeasonScreen()
        }
        binding.addNewExpenseButton.setOnClickListener {
            openAddEditExpenseScreen()
        }
        binding.viewPondDetailButton.setOnClickListener {
            openPondDetailScreen()
        }
    }

    private fun openAddEditFishPondScreen() {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditPondFragment()
        )
    }

    private fun openAddEditFarmingSeasonScreen() {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditSeasonFragment()
        )
    }

    private fun openAddEditExpenseScreen() {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToAddEditExpenseFragment()
        )
    }

    private fun openPondDetailScreen() {
        navController.navigate(
            FishFarmerRecordBookHomeFragmentDirections.actionHomeFragmentToPondDetailFragment()
        )
    }

    private fun observeViewModel() {

    }

    companion object {
        const val REQUEST_KEY_ADD_NEW_POND = "request_key.ADD_NEW_POND"
        const val BUNDLE_KEY_NEW_POND_CREATED = "bundle_key.NEW_POND_CREATED"
        const val BUNDLE_KEY_POND_ID = "bundle_key.POND_ID"
    }


}