package greenway_myanmar.org.features.farmingrecord.qr.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import greenway_myanmar.org.R

object QrUtil {

    fun generateQr(qrData: String, width: Int, height: Int, overlayDrawable: Drawable? = null): Bitmap {

        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        val bitMatrix = MultiFormatWriter().encode(
            qrData,
            BarcodeFormat.QR_CODE,
            width,
            height,
            hints
        )

        val qrBitmap = bitMatrix.toBitmap()
        val overlayBitmap = overlayDrawable?.toBitmap(
                (width * 0.3).toInt(), (height * 0.3).toInt(), Bitmap.Config.ARGB_8888
            )

        return if (overlayBitmap != null) {
            qrBitmap.addOverlay(overlayBitmap)
        } else {
            qrBitmap
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

    fun getAppLogoQrOverlay(context: Context): Drawable? {
        return AppCompatResources.getDrawable(context, R.drawable.app_logo_rounded)
    }
}