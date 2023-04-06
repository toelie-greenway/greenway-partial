package com.greenwaymyanmar.common.feature.tag.domain.usecases

import com.greenwaymyanmar.common.feature.category.domain.model.Category
import com.greenwaymyanmar.common.feature.category.domain.model.CategoryType
import com.greenwaymyanmar.common.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTagStreamUseCase @Inject constructor(

) {
    operator fun invoke(): Flow<Result<com.greenwaymyanmar.common.feature.tag.domain.model.Tag>> {
        return flow {
            emit(
                Result.Success(
                    com.greenwaymyanmar.common.feature.tag.domain.model.Tag(
                        id = "1",
                        name = "နဂါးမောက် ပင်စည်ပုပ်ရောဂါ",
                        category = Category(
                            id = "1",
                            name = "ပိုးမွှားရောဂါ",
                            imageUrl = "https://picsum.photos/id/12/960/540",
                            type = CategoryType.Agri,
                            parentCategoryId = "1"
                        ),
                        imageUrls = listOf(
                            "https://picsum.photos/id/12/960/540",
                            "https://picsum.photos/id/13/960/540",
                            "https://picsum.photos/id/14/960/540",
                        )
                    )
                )
            )
        }
    }
}