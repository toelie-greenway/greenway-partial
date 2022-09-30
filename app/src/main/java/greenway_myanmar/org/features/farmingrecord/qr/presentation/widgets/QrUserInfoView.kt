package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FarmingRecordQrUserInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.Crop
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.*


class QrUserInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FarmingRecordQrUserInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        orientation = VERTICAL
    }

    fun setData(qrInfo: UiQrInfo, user: UiQrUser) {
        binding.farmerName.text = user.name
        binding.farmerAddress.text =
            formatAddress(
                qrInfo.farmLocationType,
                user.stateRegion,
                user.township,
                user.villageTractTown,
                user.villageWard
            )
        binding.farmerContactNumber.text = if (qrInfo.optInShowPhone) {
            qrInfo.phone
        } else {
            "-"
        }
        binding.farmerCrop.text = formatCrops(user.crops)

        val avatarUrl = user.avatar.ifEmpty { null }
        Glide.with(context)
            .load(avatarUrl)
            .placeholder(R.drawable.image_placeholder)
            .fallback(R.drawable.profile_image_placeholder)
            .into(binding.farmerProfile)
    }

    private fun formatAddress(
        locationType: FarmLocationType,
        stateRegion: UiStateRegion?,
        township: UiTownship?,
        villageTractTown: UiVillageTractTown?,
        villageWard: UiVillageWard?
    ): String {
        val sb = StringBuilder()
        if (stateRegion != null) {
            sb.append(stateRegion.name)
        }

        if (township != null) {
            if (sb.isNotEmpty()) {
                sb.append("၊ ")
            }
            sb.append(township.name)
        }

        if (locationType == FarmLocationType.Township) {
            return sb.toString()
        }

        if (villageTractTown != null) {
            if (sb.isNotEmpty()) {
                sb.append("၊ ")
            }
            sb.append(villageTractTown.name)
        }

        if (villageWard != null) {
            if (sb.isNotEmpty()) {
                sb.append("၊ ")
            }
            sb.append(villageWard.name)
        }

        return sb.toString()
    }

    private fun formatCrops(crops: List<Crop>): String {
        val sb = StringBuilder()
        crops.forEachIndexed { index, crop ->
            if (index > 0) {
                sb.append("၊ ")
            }
            sb.append(crop.title)
        }
        return sb.toString()
    }
}
