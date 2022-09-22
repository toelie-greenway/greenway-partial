package greenway_myanmar.org.features.farmingrecord.qr.presentation.pickers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.common.decoration.SpaceMarginDecoration
import greenway_myanmar.org.databinding.FarmPickerFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.FarmPickerAdapter
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.ui.common.RetryCallback
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class FarmPickerFragment : Fragment(R.layout.farm_picker_fragment) {

    private var binding: FarmPickerFragmentBinding by autoCleared()
    private var adapter: FarmPickerAdapter by autoCleared()

    val viewModel: FarmPickerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FarmPickerFragmentBinding.bind(view)

        setupList()
        initList()
    }


    private fun setupList() {
        adapter = FarmPickerAdapter(object : FarmPickerAdapter.ItemClickCallback {
            override fun onItemClick(item: UiFarm) {
                navigateBackWithResult(item)
            }

        }, object : RetryCallback {
            override fun retry() {

            }
        })

        binding.farmList.addItemDecoration(SpaceMarginDecoration(requireContext(), 0, 0, 0, 8))
        binding.farmList.adapter = adapter
    }

    private fun initList() {
        viewModel.farms.observe(
            viewLifecycleOwner
        ) { listResource ->
            if (listResource == null) {
                adapter.submitList(null)
            } else {
                adapter.submitList(listResource)
            }
        }

        viewModel.networkState.observe(
            viewLifecycleOwner
        ) { networkState -> adapter.setNetworkState(networkState) }
    }

    private fun navigateBackWithResult(selected: UiFarm) {
        setFragmentResult(
            REQUEST_KEY_FARM, bundleOf(
                EXTRA_FARM to selected
            )
        )
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY_FARM = "keys.FARM"
        const val EXTRA_FARM = "extras.FARM"
    }
}