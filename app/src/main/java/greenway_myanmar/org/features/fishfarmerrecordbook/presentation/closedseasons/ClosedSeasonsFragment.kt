package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.closedseasons

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrClosedSeasonsFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class ClosedSeasonsFragment : Fragment(R.layout.ffr_closed_seasons_fragment){

    private val viewModel: ClosedSeasonsViewModel by viewModels()
    private var binding: FfrClosedSeasonsFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrClosedSeasonsFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {

    }

    private fun observeViewModel() {

    }

}