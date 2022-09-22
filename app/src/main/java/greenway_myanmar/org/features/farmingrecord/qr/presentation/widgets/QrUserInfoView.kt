package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FarmingRecordQrUserInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrUser
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiQrUser


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

    fun setData(user: UiQrUser) {
        binding.farmerName.text = user.name
        binding.farmerAddress.text = "//TODO:"
        binding.farmerContactNumber.text = "//TODO:"
        binding.farmerCrop.text = getUserFriendlyCropNames()
        Glide.with(context)
            .load(user.avatar)
            .placeholder(R.drawable.image_placeholder)
            .fallback(R.drawable.profile_image_placeholder)
            .into(binding.farmerProfile)
    }

    private fun getUserFriendlyCropNames(): String {
        return "TODO://"
    }
}
