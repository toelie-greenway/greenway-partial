package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrConfirmFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditFarmingRecordQrConfirmFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrConfirmFragmentBinding>()

    private val parentViewModel: AddEditFarmingRecordQrViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            AddEditFarmingRecordQrConfirmFragmentBinding.inflate(inflater, container, false).apply {
                submitButton.setOnClickListener {
                    navigateToSuccessScreen()
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupList()
    }

    private fun setupList() {

    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                parentViewModel.uiState.map { it.farmActivities }
                    .distinctUntilChanged()
                    .collect {
                        binding.farmingInfoView.setActivities(it)
                    }
            }
        }
    }

    private fun navigateToSuccessScreen() {
        findNavController().navigate(
            AddEditFarmingRecordQrFragmentDirections.actionAddEditFarmingRecordQrFragmentToFarmingRecordQrOrderSuccessFragment()
        )
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrConfirmFragment()
    }
}
