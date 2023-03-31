package com.greenwaymyanmar.common.feature.tag.domain.usecases

import com.greenwaymyanmar.common.feature.tag.domain.model.TagPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import javax.inject.Inject

class GetTagPostsStreamUseCase @Inject constructor(

) {
    operator fun invoke(): Flow<List<TagPost>> {
        return flow {
            emit(
                listOf(
                    TagPost(
                        "1",
                        "အဲလိုဖြစ်နေတာ ဘာဆေးသုံးရမလဲ ဘယ်လိုလုပ်ရမလဲ ပြောပေးကြပါအုံး",
                        "မိုးဇလ",
                        "https://picsum.photos/id/12/960/540",
                        Clock.System.now()
                    ),
                    TagPost(
                        "2",
                        "အဲလိုဖြစ်နေတာ ဘာဆေးသုံးရမလဲ ဘယ်လိုလုပ်ရမလဲ ပြောပေးကြပါအုံး",
                        "တိုးတိုးခိုင်",
                        "https://picsum.photos/id/12/960/540",
                        Clock.System.now()
                    ),
                    TagPost(
                        "3",
                        "အဲလိုဖြစ်နေတာ ဘာဆေးသုံးရမလဲ ဘယ်လိုလုပ်ရမလဲ ပြောပေးကြပါအုံး",
                        "မိုးဇလ",
                        null,
                        Clock.System.now()
                    ),
                )
            )
        }
    }
}