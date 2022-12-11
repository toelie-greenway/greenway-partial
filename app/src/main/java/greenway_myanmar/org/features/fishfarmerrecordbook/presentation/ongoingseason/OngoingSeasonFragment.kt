package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.ongoingseason

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbAddEditExpenseFragmentBinding
import greenway_myanmar.org.databinding.FfrbOngoingSeasonFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class OngoingSeasonFragment : Fragment(R.layout.ffrb_ongoing_season_fragment){

    private val viewModel: OngoingSeasonViewModel by viewModels()
    private var binding: FfrbOngoingSeasonFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbOngoingSeasonFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {

    }

    private fun observeViewModel() {

    }

}