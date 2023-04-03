package com.greenwaymyanmar.common.feature.tag.domain.usecases

import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import com.greenwaymyanmar.common.feature.tag.domain.repository.TagRepository
import greenway_myanmar.org.vo.Listing
import javax.inject.Inject

class GetTagsListingUseCase  @Inject constructor(
    private val repository: TagRepository
) {
    operator fun invoke(): Listing<Tag> {
        return repository.getTagsListing()
    }
}