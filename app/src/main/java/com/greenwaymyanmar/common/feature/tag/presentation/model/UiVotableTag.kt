package com.greenwaymyanmar.common.feature.tag.presentation.model

import android.os.Parcelable
import com.greenwaymyanmar.common.feature.category.presentation.model.UiCategory
import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiVotableTag(
    val tag: UiTag,
    val voteCount: Int,
    val isVoted: Boolean,
    val suggestedCategory: UiCategory?
): Parcelable {
    companion object {
        fun fromDomainModel(tag: Tag) = UiVotableTag(
            tag = UiTag.fromDomainModel(tag),
            voteCount = 0,
            isVoted = false,
            suggestedCategory = null
        )
    }
}
