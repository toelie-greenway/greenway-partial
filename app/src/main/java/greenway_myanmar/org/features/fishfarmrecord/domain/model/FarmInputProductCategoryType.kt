package greenway_myanmar.org.features.fishfarmrecord.domain.model

enum class FarmInputProductCategoryType(val key: String) {
    Aquaculture("aquaculture"),
    Default("default");

    companion object {
        fun fromString(key: String) = when (key) {
            Aquaculture.key -> Aquaculture
            else -> Default
        }
    }
}