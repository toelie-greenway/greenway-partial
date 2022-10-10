package greenway_myanmar.org.features.farmingrecord.qr.presentation.widgets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import greenway_myanmar.org.databinding.FarmingRecordQrFarmingInfoViewBinding
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.FarmLocationType
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarm
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiFarmActivity
import greenway_myanmar.org.features.farmingrecord.qr.presentation.model.UiSeason

class QrFarmingInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var farm: UiFarm? = null

    private val binding = FarmingRecordQrFarmingInfoViewBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        orientation = VERTICAL
        binding.farmLocationShowMapButton.setOnClickListener {
            val latitude = farm?.latitude
            val longitude = farm?.longitude
            if (latitude != null && longitude != null) {
                openMap(latitude, longitude)
            }
        }
    }

    private fun openMap(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(context.packageManager)?.let {
            context.startActivity(mapIntent)
        }
    }

    fun setData(
        activities: List<UiFarmActivity>,
        farm: UiFarm,
        season: UiSeason,
        farmLocationType: FarmLocationType
    ) {
        setFarmingInfo(season, activities)
        setFarm(farm, farmLocationType)
        setSeason(season)
    }

    private fun setFarmingInfo(season: UiSeason, activities: List<UiFarmActivity>) {
        binding.farmInputInfoView.setData(season, activities)
    }

    private fun setFarm(farm: UiFarm, farmLocationType: FarmLocationType) {
        this.farm = farm
        binding.farmName.text = farm.name

        if (farm.hasLatLng() && farmLocationType == FarmLocationType.Map) {
            binding.farmLocation.isVisible = false
            binding.farmLocationMapFrame.isVisible = true
        } else {
            binding.farmLocation.isVisible = true
            binding.farmLocationMapFrame.isVisible = false
            binding.farmLocation.text = "-"
        }
    }

    private fun setSeason(season: UiSeason) {
        binding.cropName.text = season.crop.title
    }

}
