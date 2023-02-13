package greenway_myanmar.org.features.fishfarmrecord.domain.model

data class FarmInputProductCategory(
    val id: String,
    val name: String,
    val thumbnail: String,
    val type: FarmInputProductCategoryType,
    val isFingerling: Boolean
)