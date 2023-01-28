package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.model.asString
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrbPondListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import java.math.BigDecimal

class FarmListItemViewHolder(
    private val parent: ViewGroup,
    private val onItemClick: (view: View, item: FarmListItemUiState) -> Unit,
    private val onCompanyClick: (company: ContractFarmingCompany) -> Unit,
    private val onAddNewSeasonClick: (view: View, item: FarmListItemUiState) -> Unit,
    private val onAddNewExpenseClick: (item: FarmListItemUiState) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffrb_pond_list_item, parent, false)
) {
    private val binding = FfrbPondListItemBinding.bind(itemView)
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


    fun bind(item: FarmListItemUiState) {
        bindContainerCardView(item)
        bindThumbnailImage(item.thumbnailImageUrl)
        bindPondName(item.name)
        bindOngoingSeason(item.ongoingSeason)
        bindContractFarmingCompany(item.contractFarmingCompany)
        bindNewSeasonButton(item)
        bindNewExpenseButton(item)
        bindTotalExpenses(item.hasOngoingSeason, item.ongoingSeason?.totalExpenses)
        bindPondArea(item.area)

        setContainerCardViewTransitionName(item)
        setNewSeasonButtonTransitionName(item)
    }

    private fun bindContainerCardView(item: FarmListItemUiState) {
        binding.containerCardView.setOnClickListener {
            onItemClick(it, item)
        }
    }

    private fun bindPondName(pondName: String) {
        binding.pondName.text = pondName
    }

    private fun bindThumbnailImage(thumbnailImageUrl: String?) {
        Glide.with(parent.context)
            .load(thumbnailImageUrl)
            .placeholder(R.drawable.image_placeholder)
            .fallback(R.drawable.farm_greyscale_placeholder)
            .into(binding.pondThumbnailImage)
    }

    private fun bindOngoingSeason(ongoingSeason: Season?) {
        if (ongoingSeason != null) {
            binding.seasonName.text = ongoingSeason.name
        } else {
            binding.seasonName.setText(R.string.ffrb_label_no_ongoing_season)
        }
    }

    private fun bindContractFarmingCompany(company: ContractFarmingCompany?) {
        if (company != null) {
            binding.companyName.text = company.name
            binding.companyName.isVisible = true
            binding.companyName.setOnClickListener {
                onCompanyClick(company)
            }
        } else {
            binding.companyName.isVisible = false
            binding.companyName.setOnClickListener(null)
        }
    }

    private fun bindNewSeasonButton(item: FarmListItemUiState) {
        val hasOngoingSeason = item.hasOngoingSeason
        if (!hasOngoingSeason) {
            binding.createNewSeasonButton.isVisible = true
            binding.createNewSeasonButton.setOnClickListener {
                onAddNewSeasonClick(it, item)
            }
        } else {
            binding.createNewSeasonButton.isVisible = false
            binding.createNewSeasonButton.setOnClickListener(null)
        }
    }

    private fun bindNewExpenseButton(item: FarmListItemUiState) {
        val hasOngoingSeason = item.hasOngoingSeason
        if (hasOngoingSeason) {
            binding.addExpenseButton.isVisible = true
            binding.addExpenseButton.setOnClickListener {
                onAddNewExpenseClick(item)
            }
        } else {
            binding.addExpenseButton.isVisible = false
            binding.addExpenseButton.setOnClickListener(null)
        }
    }

    private fun bindTotalExpenses(
        hasOngoingSeason: Boolean,
        totalExpenses: BigDecimal?
    ) {
        binding.totalExpenses.isVisible = hasOngoingSeason
        binding.totalExpenses.setAmount(totalExpenses)
    }

    private fun bindPondArea(area: UiArea?) {
        if (area != null) {
            binding.pondArea.text = area.asString(parent.context)
            binding.pondArea.isVisible = true
        } else {
            binding.pondArea.isVisible = false
        }
    }

    private fun setContainerCardViewTransitionName(item: FarmListItemUiState) {
        val transactionName =
            context.resources.getString(
                R.string.ffr_transition_name_list_item_farm_detail,
                item.id
            )
        ViewCompat.setTransitionName(binding.containerCardView, transactionName)
    }

    private fun setNewSeasonButtonTransitionName(item: FarmListItemUiState) {
        val transactionName =
            context.resources.getString(R.string.ffr_transition_name_list_item_add_season, item.id)
        ViewCompat.setTransitionName(binding.createNewSeasonButton, transactionName)
    }

}