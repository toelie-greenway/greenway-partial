package greenway_myanmar.org.features.fishfarmrecord.domain.model

enum class FishSize(val key: String) {
    Large("large"),
    Medium("medium"),
    Small("small"),
    HomePresent("home-present");

    companion object {
        fun fromString(key: String) = when (key) {
            Large.key -> Large
            Medium.key -> Medium
            Small.key -> Small
            HomePresent.key -> HomePresent
            else -> {
                throw IllegalArgumentException("Invalid fish size key: $key")
            }
        }
    }
}