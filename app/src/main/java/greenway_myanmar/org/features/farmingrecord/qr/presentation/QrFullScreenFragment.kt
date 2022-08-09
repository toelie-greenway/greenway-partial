package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.QrFullScreenFragmentBinding
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.QrUtil
import greenway_myanmar.org.util.kotlin.autoCleared

@AndroidEntryPoint
class QrFullScreenFragment : Fragment() {

    private var binding by autoCleared<QrFullScreenFragmentBinding>()

    private val args by navArgs<QrFullScreenFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QrFullScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showQr()
    }

    private fun showQr() {
        val qrDimension = resources.getDimensionPixelSize(R.dimen.qr_size)
        binding.photoView.setImageBitmap(
            QrUtil.generateQr(
                args.qrData,
                qrDimension,
                qrDimension,
                QrUtil.getAppLogoQrOverlay(requireContext())
            )
        )
    }
}