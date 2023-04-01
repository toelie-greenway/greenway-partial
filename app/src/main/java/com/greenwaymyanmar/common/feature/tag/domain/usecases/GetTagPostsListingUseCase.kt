package com.greenwaymyanmar.common.feature.tag.domain.usecases

import greenway_myanmar.org.repository.PostRepository
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.Post
import javax.inject.Inject

class GetTagPostsListingUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Listing<Post> {
        return repository.getPostsByTag()
    }
}