package greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbAddEditSeasonFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.views.GreenWayFishListInputView
import greenway_myanmar.org.features.fishfarmrecord.presentation.addeditseason.views.GreenWayFishListInputView.OnItemClickListener
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput.FishInputFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.themeColor
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditSeasonFragment : Fragment(R.layout.ffrb_add_edit_season_fragment) {

    private val viewModel: AddEditSeasonViewModel by viewModels()
    private var binding: FfrbAddEditSeasonFragmentBinding by autoCleared()
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FfrbAddEditSeasonFragmentBinding.bind(view)
        ViewCompat.setTransitionName(view, getString(R.string.ffr_transition_name_screen_add_edit_season))
        setupFragmentResultListener()
        setupToolbar()
        setupUi()
        observeViewModel()
    }

    private fun setupTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // The drawing view is the id of the view above which the container transform
            // will play in z-space.
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.greenway_motion_duration_large).toLong()
            // Set the color of the scrim to transparent as we also want to animate the
            // list fragment out of view
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(FishInputFragment.REQUEST_KEY_FISH) { _, bundle ->
            val fish = bundle.getParcelableExtraCompat<UiFish>(FishInputFragment.KEY_FISH)
            if (fish != null) {
                viewModel.handleEvent(
                    AddEditSeasonUiEvent.OnFishAdded(fish)
                )
            }
        }
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
        setupFishListInputView()
        setupSubmitButton()
    }

    private fun setupFishListInputView() {
        binding.fishListInputView.setOnDataSetChangeListener(object :
            GreenWayFishListInputView.OnDataSetChangeListener {
            override fun onDataSetChanged(items: List<UiFish>) {
                viewModel.handleEvent(AddEditSeasonUiEvent.OnFishListChanged(items))
            }
        })
        binding.fishListInputView.setOnItemClickListener(object : OnItemClickListener {
            override fun onAddNewFishClick() {
                openFishInputScreen()
            }

            override fun onFishItemClick() {
            }
        })
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFishes()
        }
    }

    private fun CoroutineScope.observeFishes() = launch {
        viewModel.uiState.map { it.fishes }
            .distinctUntilChanged()
            .collect {
                binding.fishListInputView.setItems(it)
            }
    }

    private fun onSubmit() {
        navController.popBackStack()
    }

    private fun openFishInputScreen() {
        findNavController().navigate(
            AddEditSeasonFragmentDirections.actionAddEditSeasonFragmentToFishInputFragment()
        )
    }

}