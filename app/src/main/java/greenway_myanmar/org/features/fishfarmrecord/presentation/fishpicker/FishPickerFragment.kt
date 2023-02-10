package greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FishPickerFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.epoxycontroller.FishPickerController
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.ui.widget.GreenWayToast
import greenway_myanmar.org.util.NetworkManager
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FishPickerFragment : Fragment() {

    lateinit var controller: FishPickerController

    private val viewModel: FishPickerViewModel by viewModels()

    private var binding: FishPickerFragmentBinding by autoCleared()

    private val autoSearchHandler = Handler(Looper.getMainLooper()) { _ ->
        search()
        true
    }

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding: FishPickerFragmentBinding = FishPickerFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreviousBackStackSavedState()
        setupUi()
        observeViewModel()
    }

    private fun initPreviousBackStackSavedState() {
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle[KEY_PICK_FISH_SUCCESS] = false
    }

    private fun setupUi() {
        setupToolbar()
        setupRecyclerView()
        initUserCrops()
        setupSearchView()
    }

    private fun setupToolbar() {
//        setupToolbar(binding.toolbar, true)
//        binding.toolbar.setNavigationOnClickListener { navigateUpOrFinish(view) }
    }

    private fun setupRecyclerView() {
        controller = FishPickerController(object : FishPickerController.ClickCallback {
            override fun onItemClick(selectedFish: UiFish) {
                viewModel.handleEvent(
                    FishPickerEvent.ToggleFishSelection(selectedFish.id)
                )
                setResult(selectedFish)
            }
        })
        binding.epoxyRecyclerView.apply {
            setController(controller)
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(EpoxyItemSpacingDecorator(8.dp(requireContext())))
        }
    }

    private fun initUserCrops() {
//        viewModel.userCrops.observe(
//            viewLifecycleOwner,
//            Observer { listResource ->
//                listResource?.let { controller.setUserCropsResource(listResource) }
//            }
//        )
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let { delayedSearch(AUTO_SEARCH_DELAY_MILLIS) }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    cancelDelayedSearch()
                }
            }
        )
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFishes()
        }
    }

    private fun CoroutineScope.observeFishes() = launch {
        viewModel.fishesUiState.collect {
            controller.setFishesUiState(it)
            binding.loadingStateView.bind(it)
        }
    }

    private fun delayedSearch(delayMillis: Long) {
        autoSearchHandler.removeMessages(0)
        autoSearchHandler.sendEmptyMessageDelayed(0, delayMillis)
    }

    private fun cancelDelayedSearch() {
        autoSearchHandler.removeMessages(0)
    }

    private fun search() {
        if (NetworkManager.isOnline(requireContext())) {
            performSearch()
        } else {
            GreenWayToast.showToast(
                requireContext(),
                resources.getString(R.string.error_no_network),
                Toast.LENGTH_SHORT
            )
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString()
        updateQuery(query)
    }

    private fun updateQuery(query: String) {
        viewModel.setQuery(query)
    }

    private fun setResult(item: UiFish) {
        savedStateHandle[KEY_PICK_FISH_SUCCESS] = true
        setFragmentResult(REQUEST_KEY_FISH, bundleOf(KEY_FISH to item))
        findNavController().popBackStack()
    }

    companion object {

        const val REQUEST_KEY_FISH = "request_key.FISH"
        const val KEY_FISH = "key.FISH"
        const val KEY_PICK_FISH_SUCCESS = "key.PICK_FISH_SUCCESS"

        private const val AUTO_SEARCH_DELAY_MILLIS = 700L

        @JvmStatic
        fun newInstance() = FishPickerFragment().apply { arguments = Bundle().apply {} }
    }
}
