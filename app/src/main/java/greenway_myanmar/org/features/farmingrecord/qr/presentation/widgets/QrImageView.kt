package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.core.view.doOnLayout
import com.google.android.material.imageview.ShapeableImageView
import greenway_myanmar.org.features.farmingrecord.qr.presentation.util.QrUtil

class QrImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    fun setQrData(data: String) {
        if (data.isNotEmpty()) {
            doOnLayout {
                setImageBitmap(generateQr(data))
            }
        } else {
            setImageDrawable(null)
        }
    }

    private fun generateQr(data: String): Bitmap {
        return QrUtil.generateQr(data, width, height, QrUtil.getAppLogoQrOverlay(context))
    }
}