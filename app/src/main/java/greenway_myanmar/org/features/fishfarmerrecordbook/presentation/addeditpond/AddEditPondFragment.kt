package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.addeditpond

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbAddEditPondFragmentBinding
import greenway_myanmar.org.databinding.FfrbHomeFragmentBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home.FishFarmerRecordBookHomeFragment
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home.FishFarmerRecordBookHomeViewModel
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditPondFragment : Fragment(R.layout.ffrb_add_edit_pond_fragment) {

    private val viewModel: FishFarmerRecordBookHomeViewModel by viewModels()
    private var binding: FfrbAddEditPondFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbAddEditPondFragmentBinding.bind(view)
        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.ffrb_add_edit_pond)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_done) {
                onSubmit()
                true
            } else {
                false
            }
        }
    }

    private fun setupUi() {
        binding.submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun observeViewModel() {

    }

    private fun onSubmit() {
        setFragmentResult(
            FishFarmerRecordBookHomeFragment.REQUEST_KEY_ADD_NEW_POND, bundleOf(
                FishFarmerRecordBookHomeFragment.BUNDLE_KEY_NEW_POND_CREATED to true,
                FishFarmerRecordBookHomeFragment.BUNDLE_KEY_POND_ID to "212"
            )
        )
        navController.popBackStack()
    }

}