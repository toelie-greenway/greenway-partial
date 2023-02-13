package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.core.presentation.model.LoadingState
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FfrFarmInputProductPickerFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFarmInputProduct
import greenway_myanmar.org.ui.widget.GreenWayToast
import greenway_myanmar.org.util.NetworkManager
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmInputProductPickerFragment : Fragment() {

    private val viewModel: FarmInputProductPickerViewModel by viewModels()

    private var binding: FfrFarmInputProductPickerFragmentBinding by autoCleared()

    private var adapter: FarmInputProductAdapter by autoCleared()

    private val navController: NavController by lazy {
        findNavController()
    }

    private val autoSearchHandler = Handler(Looper.getMainLooper()) { msg ->
        search()
        true
    }

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FfrFarmInputProductPickerFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreviousBackStackSavedState()
        setupUi()
        observeViewModel()
    }

    private fun initPreviousBackStackSavedState() {
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle[KEY_PICK_PRODUCT_SUCCESS] = false
    }

    private fun setupUi() {
        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupCategoryFilterView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = FarmInputProductAdapter(
            itemClickCallback = object : FarmInputProductAdapter.ItemClickCallback {
                override fun onItemClick(item: UiFarmInputProduct) {
                    setResult(item)
                }
            }
        )

        binding.list.apply {
            addItemDecoration(SpaceMarginDecoration(requireContext(), 8))
            setHasFixedSize(true)
            this.adapter = this@FarmInputProductPickerFragment.adapter
        }
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

    private fun setupCategoryFilterView() {
        binding.categoryAutoCompleteTextView.apply {
            setAdapter(emptyAdapter())
            onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    viewModel.handleEvent(
                        FarmInputProductPickerEvent.OnCategorySelectionChanged(
                            position
                        )
                    )
                }
        }
    }

    private fun emptyAdapter() = ArrayAdapter<String>(
        requireContext(),
        R.layout.greenway_dropdown_menu_popup_item,
        mutableListOf()
    )

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeCategories()
            observeProducts()
            observeSelectedCategory()
        }
    }

    private fun CoroutineScope.observeSelectedCategory() = launch {
        viewModel.uiState.map { it.category }
            .distinctUntilChanged()
            .collect {
                binding.categoryAutoCompleteTextView.setText(it?.name.orEmpty(), false)
            }
    }

    private fun CoroutineScope.observeProducts() = launch {
        viewModel.productsUiState.collect { uiState ->
            when (uiState) {
                is LoadingState.Success -> {
                    adapter.submitList(uiState.data)
                }
                else -> {
                    adapter.submitList(emptyList())
                }
            }
        }
    }

    private fun CoroutineScope.observeCategories() = launch {
        viewModel.categoriesUiState
            .collect { uiState ->
                when (uiState) {
                    is LoadingState.Success -> {
                        binding.categoryAutoCompleteTextView.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                R.layout.greenway_dropdown_menu_popup_item,
                                uiState.data.map { it.name }
                            )
                        )
                    }
                    else -> {
                        binding.categoryAutoCompleteTextView.setAdapter(emptyAdapter())
                    }
                }
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
        viewModel.handleEvent(FarmInputProductPickerEvent.OnQueryChanged(query))
    }

    private fun setResult(item: UiFarmInputProduct) {
        savedStateHandle[KEY_PICK_PRODUCT_SUCCESS] = true
        setFragmentResult(
            REQUEST_KEY_PRODUCT, bundleOf(
                KEY_PRODUCT to item,
            )
        )
        findNavController().popBackStack()
    }

    companion object {

        private const val AUTO_SEARCH_DELAY_MILLIS = 1000L

        const val REQUEST_KEY_PRODUCT = "request_key.PRODUCT"
        const val KEY_PRODUCT = "key.PRODUCT"
        const val KEY_PICK_PRODUCT_SUCCESS = "key.PICK_PRODUCT_SUCCESS"
    }
}
