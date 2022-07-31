package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFormFragmentBinding
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.adapters.AddEditQrPagerAdapter
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditFarmingRecordQrFormFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrFormFragmentBinding>()

    private val parentViewModel: AddEditFarmingRecordQrViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            AddEditFarmingRecordQrFormFragmentBinding.inflate(inflater, container, false).apply {
                submitButton.setOnClickListener {
                    parentViewModel.handleEvent(AddEditFarmingRecordQrEvent.PageChanged(AddEditQrPagerAdapter.CONFIRM_PAGE_INDEX))
                }
            }
        return binding.root
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrFormFragment()
    }
}
