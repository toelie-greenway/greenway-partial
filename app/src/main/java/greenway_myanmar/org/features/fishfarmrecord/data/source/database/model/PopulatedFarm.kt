package greenway_myanmar.org.features.fishfarmrecord.data.source.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class PopulatedFarm(
    @Embedded
    val farm: FfrFarmEntity,
    @Relation(
        parentColumn = "opening_season_id",
        entityColumn = "id"
    )
    val season: FfrSeasonEntity? = null
)
//data class PopulatedFarm(
//    @Embedded
//    var farm: FfrFarmEntity,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "opening_season_id"
//    )
//    var season: FfrSeasonEntity? = null
//) {
//    constructor() : this() {
//
//    }
//}
//
fun PopulatedFarm.asDomainModel() = farm.asDomainModel(openingSeason = season)