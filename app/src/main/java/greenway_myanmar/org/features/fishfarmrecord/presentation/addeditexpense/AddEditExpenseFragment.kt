package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditexpense

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbAddEditExpenseFragmentBinding
import greenway_myanmar.org.databinding.FfrbPondDetailFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditExpenseFragment : Fragment(R.layout.ffrb_add_edit_expense_fragment){

    private val viewModel: AddEditExpenseViewModel by viewModels()
    private var binding: FfrbAddEditExpenseFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbAddEditExpenseFragmentBinding.bind(view)

        setupUi()
        observeViewModel()
    }

    private fun setupUi() {

    }

    private fun observeViewModel() {

    }

}