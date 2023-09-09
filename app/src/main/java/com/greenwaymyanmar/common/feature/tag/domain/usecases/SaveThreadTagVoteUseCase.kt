package com.greenwaymyanmar.common.feature.tag.domain.usecases

import com.greenwaymyanmar.common.feature.tag.domain.repository.TagRepository
import javax.inject.Inject

class SaveThreadTagVoteUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(request: SaveThreadTagVoteRequest): Boolean {
        return tagRepository.saveThreadTagVoting(
            tagId = request.tagId,
            threadId = request.threadId,
            categoryId = request.categoryId,
            previousVotedOptionId = request.previousVotedOptionId
        )
    }

    data class SaveThreadTagVoteRequest(
        val tagId: String,
        val threadId: String,
        val categoryId: String,
        val previousVotedOptionId: String
    )
}