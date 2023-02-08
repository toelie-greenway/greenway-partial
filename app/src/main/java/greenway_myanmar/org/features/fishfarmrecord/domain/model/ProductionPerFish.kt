package greenway_myanmar.org.features.fishfarmrecord.domain.model

data class ProductionPerFish(
    val fish: Fish,
    val productionsPerFishSize: List<ProductionPerFishSize>
)