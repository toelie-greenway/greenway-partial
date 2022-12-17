package greenway_myanmar.org.features.fishfarmerrecordbook.presentation.openingseason

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class OpeningSeasonCategoryViewHolder(
    parent: ViewGroup,
    @LayoutRes layoutResId: Int
) : ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(layoutResId, parent, false)
) {
    abstract fun bind(item: OpeningSeasonCategoryListItemUiState)
}