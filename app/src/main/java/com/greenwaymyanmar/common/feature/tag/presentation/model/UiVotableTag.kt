package com.greenwaymyanmar.common.feature.tag.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.model.VotableTag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiVotableTag(
    val id: String,
    val tag: UiTag,
    val voteCount: Int,
    val isVoted: Boolean,
    val suggestedCategory: UiCategory?
) : Parcelable {
    companion object {
        fun fromTagDomainModel(tag: Tag) = UiVotableTag(
            id = "-1",
            tag = UiTag.fromDomainModel(tag),
            voteCount = 0,
            isVoted = false,
            suggestedCategory = null
        )

        fun fromDomainModel(domainModel: VotableTag) = UiVotableTag(
            id = domainModel.id,
            tag = UiTag.fromDomainModel(domainModel.tag),
            voteCount = domainModel.votesCount,
            isVoted = domainModel.isVoted,
            suggestedCategory = if (domainModel.category != null) {
                UiCategory.fromDomainModel(domainModel.category)
            } else {
                null
            }
        )
    }
}
