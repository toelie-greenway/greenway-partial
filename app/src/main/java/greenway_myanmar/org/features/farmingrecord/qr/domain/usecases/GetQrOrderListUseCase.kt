package greenway_myanmar.org.features.farmingrecord.qr.domain.usecases

import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQrOrderListUseCase @Inject constructor(

) {

    operator fun invoke(): Flow<List<QrOrder>> {
        return flow {
            emit(
                listOf(
                    QrOrder("1", "Farm1", "a"),
                    QrOrder("2", "Farm2", ""),
                    QrOrder("3", "Farm3", "c"),
                    QrOrder("4", "Farm4", "d"),
                    QrOrder("5", "Farm5", "e"),
                    QrOrder("11", "Farm1", "a"),
                    QrOrder("12", "Farm2", ""),
                    QrOrder("13", "Farm3", "c"),
                    QrOrder("14", "Farm4", "d"),
                    QrOrder("15", "Farm5", "e"),
                    QrOrder("21", "Farm1", "a"),
                    QrOrder("22", "Farm2", ""),
                    QrOrder("23", "Farm3", "c"),
                    QrOrder("24", "Farm4", "d"),
                    QrOrder("25", "Farm5", "e"),
                    QrOrder("31", "Farm1", "a"),
                    QrOrder("32", "Farm2", ""),
                    QrOrder("33", "Farm3", "c"),
                    QrOrder("34", "Farm4", "d"),
                    QrOrder("35", "Farm5", "e"),
                    QrOrder("11", "Farm1", "a"),
                    QrOrder("12", "Farm2", ""),
                    QrOrder("13", "Farm3", "c"),
                    QrOrder("14", "Farm4", "d"),
                    QrOrder("15", "Farm5", "e"),
                    QrOrder("111", "Farm1", "a"),
                    QrOrder("112", "Farm2", ""),
                    QrOrder("113", "Farm3", "c"),
                    QrOrder("114", "Farm4", "d"),
                    QrOrder("115", "Farm5", "e"),
                    QrOrder("121", "Farm1", "a"),
                    QrOrder("122", "Farm2", ""),
                    QrOrder("123", "Farm3", "c"),
                    QrOrder("124", "Farm4", "d"),
                    QrOrder("125", "Farm5", "e"),
                    QrOrder("131", "Farm1", "a"),
                    QrOrder("132", "Farm2", ""),
                    QrOrder("133", "Farm3", "c"),
                    QrOrder("134", "Farm4", "d"),
                    QrOrder("135", "Farm5", "e"),
                )
            )
        }
    }

}