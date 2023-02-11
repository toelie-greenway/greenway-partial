package greenway_myanmar.org.features.fishfarmrecord.presentation.season.closedseasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrClosedSeasonListItemBinding
import greenway_myanmar.org.util.DateUtils
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

//    android:onClick="@{() -> itemClickCallback.onItemClick(item)}"
//    android:onClick="@{() -> itemClickCallback.onCompanyClick(item.openingSeason.company)}"
//    android:text='@{item.name}'
//    android:text='@{MyanmarNumberExtensionKt.toMyanmar(item.area) + " " + item.userFriendlyUnit()}'
//    android:onClick="@{() -> itemClickCallback.onAddSeasonClick(item)}"
//    app:visibleGone="@{item.openingSeason == null}"
//    android:onClick="@{() -> itemClickCallback.onAddExpenseClick(item)}"
//    app:visibleGone="@{item.openingSeason != null}"

//    app:visibleGone="@{item.openingSeason != null}"

//    app:visibleGone='@{item.pendingAction.pending}'


    fun bind(item: ClosedSeasonListItemUiState) {
        binding.pondName.text = item.seasonName
        binding.totalExpense.setAmount(item.totalExpenses)
        binding.seasonInfo.text = formatSeasonInfo(item.seasonName, item.seasonStartDate)
    }

    private fun bindThumbnailImage(thumbnailImageUrl: String?) {
//        Glide.with(context)
//            .load(thumbnailImageUrl)
//            .placeholder(R.drawable.image_placeholder)
//            .fallback(R.drawable.farm_greyscale_placeholder)
//            .into(binding.pondThumbnailImage)
    }

    private fun formatSeasonInfo(seasonName: String, seasonStartDate: Instant): String {
        return context.resources.getString(
            R.string.ffr_label_closed_season_formatted_season_info,
            seasonName,
            formatSeasonStartDate(seasonStartDate)
        )
    }

    private fun formatSeasonStartDate(seasonStartDate: Instant): String {
        return DateUtils.format(seasonStartDate.toJavaInstant(), "yyyy")
    }

}