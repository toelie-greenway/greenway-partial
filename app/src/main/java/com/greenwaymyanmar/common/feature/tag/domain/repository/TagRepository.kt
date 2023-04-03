package com.greenwaymyanmar.common.feature.tag.domain.repository

import com.greenwaymyanmar.common.feature.tag.domain.model.Tag
import greenway_myanmar.org.vo.Listing

interface TagRepository {
    fun getTagsListing(): Listing<Tag>
}