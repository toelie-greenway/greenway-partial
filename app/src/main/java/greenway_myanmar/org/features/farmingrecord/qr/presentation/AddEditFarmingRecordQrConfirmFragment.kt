package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.databinding.AddEditFarmingRecordQrConfirmFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class AddEditFarmingRecordQrConfirmFragment : Fragment() {

    private var binding by autoCleared<AddEditFarmingRecordQrConfirmFragmentBinding>()

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

    private fun navigateToSuccessScreen() {
        findNavController().navigate(
            AddEditFarmingRecordQrFragmentDirections.actionAddEditFarmingRecordQrFragmentToFarmingRecordQrOrderSuccessFragment()
        )
    }

    companion object {
        fun newInstance() = AddEditFarmingRecordQrConfirmFragment()
    }
}
