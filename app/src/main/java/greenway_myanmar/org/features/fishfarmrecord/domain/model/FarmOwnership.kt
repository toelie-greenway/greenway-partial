package greenway_myanmar.org.features.fishfarmrecord.domain.model

enum class FarmOwnership(val value: String) {
    OWN("own"),
    RENT("rent");

    companion object {
        fun fromString(value: String): FarmOwnership = when (value) {
            OWN.value -> {
                OWN
            }
            RENT.value -> {
                RENT
            }
            else -> {
                throw IllegalArgumentException("Invalid FarmOwnership enum value: $value")
            }
        }
    }
}

fun FarmOwnership.asString() = this.value
