package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RelativeCornerSize
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.model.asString
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrClosedSeasonListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asString
import greenway_myanmar.org.util.DateUtils
import greenway_myanmar.org.util.UIUtils
import greenway_myanmar.org.util.extensions.dp
import greenway_myanmar.org.util.extensions.load
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

class ClosedSeasonListItemViewHolder(
    parent: ViewGroup,
    private val onItemClick: (item: ClosedSeasonListItemUiState) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_closed_season_list_item, parent, false)
) {
    private val binding = FfrClosedSeasonListItemBinding.bind(itemView)
    private val context = parent.context

    fun bind(item: ClosedSeasonListItemUiState) {
        binding.farmName.text = item.farmName
        binding.totalProfitTextView.setAmount(item.totalProfit)
        binding.totalProfitTextView.setTextColor(
            ContextCompat.getColor(
                context,
                if (item.isProfit) {
                    R.color.color_primary
                } else {
                    R.color.color_error
                }
            )
        )
        binding.seasonInfo.text = formatSeasonInfo(item.seasonName, item.seasonStartDate)

        if (!item.farmImages.isNullOrEmpty()) {
            binding.farmImageView.load(
                context,
                item.farmImages.first()
            )
        }

        binding.fishIconsContainer.removeAllViews()
        item.fishes.forEach {
            binding.fishIconsContainer.addView(
                createFishIconView(context, it)
            )
        }

        binding.info.text = formatInfo(item.area, item.fishes)
        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }

    private fun createFishIconView(context: Context, fish: UiFish): ShapeableImageView {
        val imageView = ShapeableImageView(context)
        imageView.setPadding(1.dp(context).toInt())
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.strokeWidth = 1.dp(context)
        imageView.setStrokeColorResource(R.color.app_white)
        imageView.shapeAppearanceModel = imageView.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(RelativeCornerSize(0.5f))
            .build()
        imageView.layoutParams =
            ViewGroup.LayoutParams(UIUtils.dpToPx(context, 32), UIUtils.dpToPx(context, 32))
        imageView.load(
            context,
            fish.imageUrl
        )
        return imageView
    }

    private fun formatSeasonInfo(seasonName: String, seasonStartDate: Instant): String {
        return context.resources.getString(
            R.string.ffr_label_closed_season_formatted_season_info,
            seasonName,
            formatSeasonStartDate(seasonStartDate)
        )
    }

    private fun formatInfo(area: UiArea?, fishes: List<UiFish>): String {
        return context.resources.getString(
            R.string.ffr_label_closed_season_formatted_info,
            area?.asString(context).orEmpty(),
            fishes.asString()
        )
    }

    private fun formatSeasonStartDate(seasonStartDate: Instant): String {
        return DateUtils.format(seasonStartDate.toJavaInstant(), "yyyy")
    }
}