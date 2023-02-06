package greenway_myanmar.org.features.fishfarmrecord.presentation.expense.addeditexpense.farminput.products

import android.os.Bundle
import android.os.Handler
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
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FarmInputProductPickerFragment : Fragment() {

  private val viewModel: FarmInputProductPickerViewModel by viewModels()

  private var binding: FfrFarmInputProductPickerFragmentBinding by autoCleared()

  private var adapter: FarmInputProductAdapter by autoCleared()

  private val autoSearchHandler = Handler { msg ->
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
    initUserCrops()
    setupSearchView()
  }

  private fun setupToolbar() {
//        setupToolbar(binding.toolbar, true)
//        binding.toolbar.setNavigationOnClickListener { navigateUpOrFinish(view) }
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
      observeProducts()
    }
  }

  private fun CoroutineScope.observeProducts() = launch {
    viewModel.productsUiState.collect {uiState ->
      Timber.d("Products UI State: $uiState")
      when (uiState) {
        is LoadingState.Success -> {
          adapter.submitList(uiState.data)
        }
        else -> {

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
    setFragmentResult(REQUEST_KEY_PRODUCT, bundleOf(KEY_PRODUCT to item))
    findNavController().popBackStack()
  }

//
//  var binding by autoCleared<FfrF>()
//
//  private var adapter by autoCleared<FarmInputProductAdapter>()
//
//  val viewModel by viewModels<FarmInputProductsViewModel>()
//
//  private val autoSearchHandler = Handler { msg ->
//    search()
//    true
//  }
//
//  override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//  ): View {
//    binding =
//      DataBindingUtil.inflate(
//        inflater,
//        R.layout.asymt_farm_input_products_fragment,
//        container,
//        false
//      )
//    return binding.root
//  }
//
//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//    setupToolbar()
//    setupViewModel()
//    setupList()
//    initList()
//
//    setupCategoryDropdown()
//    initCategoryDropdown()
//
//    setupSearchView()
//  }
//
//  private fun setupToolbar() {
//    setupToolbar(binding.toolbar) { onBackPressed() }
//  }
//
//  private fun setupCategoryDropdown() {
//    binding.categoryDropdownMenu.setOnItemClickListener(
//      object : AsylExposedDropdownMenu.OnItemClickListener<ProductCategory> {
//        override fun onItemClick(selectedItem: ProductCategory) {
//          onCategorySelected(selectedItem)
//        }
//      }
//    )
//  }
//
//  private fun initCategoryDropdown() {
//    viewModel.productCategories.observe(
//      viewLifecycleOwner,
//      Observer { listResource ->
//        listResource.data?.let { list -> binding.categoryDropdownMenu.swapList(list) }
//        binding.categoryDropdownMenu.setResource(listResource)
//      }
//    )
//  }
//
//  private fun onCategorySelected(category: ProductCategory) {
//    binding.searchEditText.setText("")
//    binding.categoryDropdownMenu.setText(category.title)
//    binding.categoryDropdownMenu.setValue(category.id)
//    viewModel.setCategoryId(categoryId = category.id)
//  }
//
//  private fun onBackPressed() {
//    navigateUpOrFinish(view)
//  }
//
//  private fun setupViewModel() {
//    binding.viewModel = viewModel
//    binding.lifecycleOwner = viewLifecycleOwner
//    viewModel.start()
//  }
//
//  private fun setupList() {
//    val rvAdapter =
//      FarmInputProductAdapter(
//        object : FarmInputProductAdapter.ItemClickCallback {
//          override fun onItemClick(item: Product?) {
//            item?.let {
//              setFragmentResult(
//                REQUEST_KEY_FARM_INPUT_PRODUCT,
//                bundleOf(KEY_SELECTED_PRODUCT to it)
//              )
//              navigateUpOrFinish(view)
//            }
//          }
//        },
//        RetryCallback {}
//      )
//
//    binding.list.apply {
//      addItemDecoration(SpaceMarginDecoration(requireContext(), 8))
//      setHasFixedSize(true)
//      adapter = rvAdapter
//    }
//    adapter = rvAdapter
//  }
//
//  private fun initList() {
//    viewModel.products.observe(
//      viewLifecycleOwner,
//      Observer { listResource ->
//        if (listResource == null) {
//          adapter.submitList(null)
//        } else {
//          adapter.submitList(listResource)
//        }
//      }
//    )
//
//    viewModel.networkState.observe(
//      viewLifecycleOwner,
//      Observer { networkState -> adapter.setNetworkState(networkState) }
//    )
//  }
//
//  private fun setupSearchView() {
//    binding.searchEditText.addTextChangedListener(
//      object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
//          s?.let { delayedSearch(AUTO_SEARCH_DELAY_MILLIS) }
//        }
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//          cancelDelayedSearch()
//        }
//      }
//    )
//  }
//
//  private fun delayedSearch(delayMillis: Long) {
//    autoSearchHandler.removeMessages(0)
//    autoSearchHandler.sendEmptyMessageDelayed(0, delayMillis)
//  }
//
//  private fun cancelDelayedSearch() {
//    autoSearchHandler.removeMessages(0)
//  }
//
//  private fun search() {
//    if (NetworkManager.isOnline(requireContext())) {
//      performSearch()
//    } else {
//      GreenWayToast.showToast(
//        requireContext(),
//        resources.getString(R.string.error_no_network),
//        Toast.LENGTH_SHORT
//      )
//    }
//  }
//
//  private fun performSearch() {
//    val query = binding.searchEditText.text.toString()
//    updateQuery(query)
//  }
//
//  private fun updateQuery(keyword: String) {
//    viewModel.setKeyword(keyword)
//  }

  companion object {

    private const val AUTO_SEARCH_DELAY_MILLIS = 1000L

    const val REQUEST_KEY_PRODUCT = "request_key.PRODUCT"
    const val KEY_PRODUCT = "key.PRODUCT"
    const val KEY_PICK_PRODUCT_SUCCESS = "key.PICK_PRODUCT_SUCCESS"
  }
}
