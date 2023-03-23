package greenway_myanmar.org.features.tag.domain.usecases

import greenway_myanmar.org.features.tag.domain.model.TagThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import javax.inject.Inject

class GetTagThreadsStreamUseCase @Inject constructor(

) {
    operator fun invoke(): Flow<List<TagThread>> {
        return flow {
            emit(


                listOf(
                    TagThread(
                        "1",
                        "အဲလိုဖြစ်နေတာ ဘာဆေးသုံးရမလဲ ဘယ်လိုလုပ်ရမလဲ ပြောပေးကြပါအုံး",
                        listOf("https://picsum.photos/id/12/960/540"),
                        Clock.System.now()
                    ),
                    TagThread(
                        "2",
                        "စပါးပင်တွေကဘာရောဂါဖြစ်နေတာလဲဗျ...ဘာဆေးသုံးသင့်လဲ... ဘာလုပ်သင့်သလဲညွှန်ပြပေးစေလိုပါတယ်...",
                        emptyList(),
                        Clock.System.now()
                    ),
                    TagThread(
                        "3",
                        "အဲလိုဖြစ်နေတာ ဘာဆေးသုံးရမလဲ ဘယ်လိုလုပ်ရမလဲ ပြောပေးကြပါအုံး",
                        listOf("https://picsum.photos/id/12/960/540"),
                        Clock.System.now()
                    )
                )
            )
        }
    }
}