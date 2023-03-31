package com.greenwaymyanmar.common.feature.tag.domain.usecases

import greenway_myanmar.org.repository.ThreadRepository
import greenway_myanmar.org.ui.threads.ThreadsViewModel
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.Thread
import javax.inject.Inject

class GetTagThreadsListingUseCase @Inject constructor(
    private val threadRepository: ThreadRepository
) {
    operator fun invoke(): Listing<Thread> {
        return threadRepository.loadThreads(ThreadsViewModel.Query.empty())
    }
}