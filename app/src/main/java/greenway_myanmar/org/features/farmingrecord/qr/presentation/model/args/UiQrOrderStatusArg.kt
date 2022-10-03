package greenway_myanmar.org.features.farmingrecord.qr.presentation.model.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiQrOrderStatusArg(
    val qrOrderId: String,
    val qrId: String
) : Parcelable
