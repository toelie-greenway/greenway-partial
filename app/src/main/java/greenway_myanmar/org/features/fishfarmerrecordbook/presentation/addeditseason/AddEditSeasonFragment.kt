package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.addeditseason

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbAddEditPondFragmentBinding
import greenway_myanmar.org.databinding.FfrbAddEditSeasonFragmentBinding
import greenway_myanmar.org.features.fishfarmerrecordbook.presentation.home.FishFarmerRecordBookHomeViewModel
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditSeasonFragment : Fragment(R.layout.ffrb_add_edit_season_fragment) {

    private val viewModel: AddEditSeasonViewModel by viewModels()
    private var binding: FfrbAddEditSeasonFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbAddEditSeasonFragmentBinding.bind(view)

        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.ffrb_add_edit_season)
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
        navController.popBackStack()
    }

}