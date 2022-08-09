package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.QrTestFragmentBinding
import greenway_myanmar.org.util.kotlin.autoCleared


@AndroidEntryPoint
class QrTestFragment : Fragment() {

    private var binding by autoCleared<QrTestFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QrTestFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateQr()

        binding.qrView.setQrData("https://levelup.gitconnected.com/ci-cd-for-flutter-using-github-actions-and-fastlane-6dfc9431ee9a")
        binding.qrSmallImageView.setQrData("https://levelup.gitconnected.com/ci-cd-for-flutter-using-github-actions-and-fastlane-6dfc9431ee9a")

//        binding.qrView.post {
//
//        }
    }

    private fun generateQr() {
        val qrData = "https://greenwaymyanmar.com/about-us"
        val qrDimension = resources.getDimensionPixelSize(R.dimen.qr_size)

        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        val bitMatrix = MultiFormatWriter().encode(
            qrData,
            BarcodeFormat.QR_CODE,
            qrDimension,
            qrDimension,
            hints
        )

        val qrBitmap = bitMatrix.toBitmap()
        val overlayBitmap =
            AppCompatResources.getDrawable(requireContext(), R.drawable.app_logo_rounded)?.toBitmap(
                (qrDimension * 0.3).toInt(), (qrDimension * 0.3).toInt(), Bitmap.Config.ARGB_8888
            )

        if (overlayBitmap != null) {
            binding.qrImageView.setImageBitmap(qrBitmap.addOverlay(overlayBitmap))
        } else {
            binding.qrImageView.setImageBitmap(qrBitmap)
        }
    }

    private fun BitMatrix.toBitmap(): Bitmap {
        val onColor: Int = Color.BLACK
        val offColor: Int = Color.WHITE
        val width = this.width
        val height = this.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (this[x, y]) onColor else offColor
            }
        }
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        image.setPixels(pixels, 0, width, 0, 0, width, height)
        return image
    }

    private fun Bitmap.addOverlay(overlayBitmap: Bitmap): Bitmap {
        val overlayWidth = overlayBitmap.width
        val overlayHeight = overlayBitmap.height
        val marginLeft = (this.width * 0.5 - overlayWidth * 0.5).toFloat()
        val marginTop = (this.height * 0.5 - overlayHeight * 0.5).toFloat()
        val canvas = Canvas(this)
        canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null)
        return this
    }
}