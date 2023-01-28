package greenway_myanmar.org.features.fishfarmrecord.domain.model

const val AREA_UNIT_ACRE = "acre"

data class Area(
    val value: Double,
    val unit: String
) {
    companion object {
        fun acre(value: Double) = Area(value, AREA_UNIT_ACRE)
        fun acreOrNull(value: Double?) = if (value != null) {
            acre(value)
        } else {
            null
        }
    }
}