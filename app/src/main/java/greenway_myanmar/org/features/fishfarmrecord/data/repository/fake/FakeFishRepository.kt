package greenway_myanmar.org.features.fishfarmrecord.data.repository.fake

import greenway_myanmar.org.features.fishfarmrecord.domain.model.Fish
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeFishRepository @Inject constructor(

) : FishRepository {
    override fun getFishesStream(query: String): Flow<List<Fish>> {
        return flow {
            emit(
                if (query.isEmpty()) {
                    listOf(
                        Fish(
                            "1",
                            "ကကတစ်",
                            "https://cdn-icons-png.flaticon.com/512/811/811643.png",
                            ""
                        ),
                        Fish(
                            "2",
                            "ငါးကြင်း",
                            "https://cdn-icons-png.flaticon.com/512/1134/1134431.png",
                            ""
                        )
                    )
                } else {
                    listOf(
                        Fish(
                            "1",
                            "ကကတစ်",
                            "https://cdn-icons-png.flaticon.com/512/811/811643.png",
                            ""
                        ),
                        Fish(
                            "2",
                            "ငါးကြင်း",
                            "https://cdn-icons-png.flaticon.com/512/1134/1134431.png",
                            ""
                        ),
                        Fish(
                            "3",
                            "ငါးသလောက်",
                            "https://cdn-icons-png.flaticon.com/512/1134/1134432.png",
                            ""
                        )
                    )
                }
            )
        }
    }
}