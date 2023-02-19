package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.model.asString
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrClosedSeasonListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.asString
import greenway_myanmar.org.util.DateUtils
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

    init {
    }

    fun bind(item: ClosedSeasonListItemUiState) {
        binding.farmName.text = item.farmName
        binding.totalExpense.setAmount(item.totalExpenses)
        binding.seasonInfo.text = formatSeasonInfo(item.seasonName, item.seasonStartDate)

        if (!item.farmImages.isNullOrEmpty()) {
            binding.farmImageView.load(
                context,
                item.farmImages.first()
            )
        }
        binding.info.text = formatInfo(item.area, item.fishes)
        binding.root.setOnClickListener {
            onItemClick(item)
        }
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