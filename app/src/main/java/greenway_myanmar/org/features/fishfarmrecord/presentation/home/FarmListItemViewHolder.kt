package greenway_myanmar.org.features.fishfarmrecord.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.greenwaymyanmar.core.presentation.model.UiArea
import com.greenwaymyanmar.core.presentation.util.numberFormat
import com.greenwaymyanmar.vo.PendingAction
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFarmListItemBinding
import greenway_myanmar.org.features.fishfarmrecord.domain.model.ContractFarmingCompany
import greenway_myanmar.org.features.fishfarmrecord.domain.model.season.Season
import greenway_myanmar.org.util.extensions.load
import java.math.BigDecimal


class FarmListItemViewHolder(
    private val parent: ViewGroup,
    private val onItemClick: (view: View, item: FarmListItemUiState) -> Unit,
    private val onCompanyClick: (company: ContractFarmingCompany) -> Unit,
    private val onAddNewSeasonClick: (view: View, item: FarmListItemUiState) -> Unit,
    private val onAddNewExpenseClick: (view: View, item: FarmListItemUiState) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.ffr_farm_list_item, parent, false)
) {
    private val binding = FfrFarmListItemBinding.bind(itemView)
    private val context = parent.context

    fun bind(item: FarmListItemUiState) {
        bindContainerCardView(item)
        bindThumbnailImage(item.thumbnailImageUrl)
        bindFarmName(item.name)
        bindOngoingSeason(item.openingSeason)
        bindContractFarmingCompany(item.openingSeason?.company)
        bindNewSeasonButton(item)
        bindNewExpenseButton(item)
        bindTotalExpenses(item.hasOngoingSeason, item.openingSeason?.totalExpenses)
        bindFarmMeasurement(item.area, item.depth)
        bindLoadingIndicator(item.pendingAction)

        setContainerCardViewTransitionName(item)
        setNewSeasonButtonTransitionName(item)
        setNewExpenseButtonTransitionName(item)
    }

    private fun bindLoadingIndicator(pendingAction: PendingAction?) {
        binding.loadingContainer.isVisible = pendingAction?.isPending() == true
    }

    private fun bindContainerCardView(item: FarmListItemUiState) {
        binding.containerCardView.setOnClickListener {
            onItemClick(it, item)
        }
    }

    private fun bindFarmName(farmName: String) {
        binding.farmName.text = farmName
    }

    private fun bindThumbnailImage(thumbnailImageUrl: String?) {
        binding.farmThumbnailImage.load(
            context = parent.context,
            imageUrl = thumbnailImageUrl,
            placeholderResourceId = R.drawable.image_placeholder,
            fallbackResourceId = R.drawable.farm_greyscale_placeholder
        )
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
            binding.addExpenseButton.setOnClickListener { view ->
                onAddNewExpenseClick(view, item)
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

    private fun bindFarmMeasurement(area: UiArea?, depth: Double?) {
        if (area != null) {
            binding.farmArea.text = if (depth != null) {
                context.getString(
                    R.string.ffr_formatted_farm_area_with_depth,
                    numberFormat.format(area.value),
                    numberFormat.format(depth)
                )
            } else {
                context.getString(
                    R.string.ffr_formatted_farm_area,
                    numberFormat.format(area.value)
                )
            }
            binding.farmArea.isVisible = true
        } else {
            binding.farmArea.isVisible = false
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

    private fun setNewExpenseButtonTransitionName(item: FarmListItemUiState) {
        val transactionName =
            context.resources.getString(R.string.ffr_transition_name_list_item_add_expense, item.id)
        ViewCompat.setTransitionName(binding.addExpenseButton, transactionName)
    }

}