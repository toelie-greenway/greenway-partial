package greenway_myanmar.org.features.tag.domain.usecases

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.tag.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTagStreamUseCase @Inject constructor(

) {
    operator fun invoke(): Flow<Result<Tag>> {
        return flow {
            emit(
                Result.Success(
                    Tag(
                        id = "1",
                        name = "နဂါးမောက် ပင်စည်ပုပ်ရောဂါ",
                        category = "စိုက်ပျိုးရေ",
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