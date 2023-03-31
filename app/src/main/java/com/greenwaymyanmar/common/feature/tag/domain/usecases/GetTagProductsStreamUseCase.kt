package com.greenwaymyanmar.common.feature.tag.domain.usecases

import com.greenwaymyanmar.common.feature.tag.domain.model.TagProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTagProductsStreamUseCase @Inject constructor(

) {
    operator fun invoke(): Flow<List<TagProduct>> {
        return flow {
            emit(
                listOf(
                    TagProduct(
                        "1",
                        "မာလာမြိုင် မန်ကိုဇက် ၈၀% ဒဗလျူပီ",
                        "မာလာမြိုင်",
                        listOf("https://picsum.photos/id/12/960/540"),
                    ),
                    TagProduct(
                        "2",
                        "မာလာမြိုင် မန်ကိုဇက် ၈၀% ဒဗလျူပီ",
                        "မာလာမြိုင်",
                        listOf("https://picsum.photos/id/13/960/540"),
                    ),
                    TagProduct(
                        "3",
                        "မာလာမြိုင် မန်ကိုဇက် ၈၀% ဒဗလျူပီ",
                        "မာလာမြိုင်",
                        emptyList(),
                    ),
                    TagProduct(
                        "4",
                        "မာလာမြိုင် မန်ကိုဇက် ၈၀% ဒဗလျူပီ",
                        "မာလာမြိုင်",
                        listOf("https://picsum.photos/id/13/960/540"),
                    ),
                )
            )
        }
    }
}