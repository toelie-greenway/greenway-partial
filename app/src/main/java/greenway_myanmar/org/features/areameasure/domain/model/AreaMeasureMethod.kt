package greenway_myanmar.org.features.areameasure.domain.model

enum class AreaMeasureMethod(val value: String) {
    Pin("pin"),
    Walk("walk"),
    Draw("draw");

    companion object {
        fun fromString(value: String?): AreaMeasureMethod = when (value) {
            Pin.value -> {
                Pin
            }
            Walk.value -> {
                Walk
            }
            Draw.value -> {
                Draw
            }
            else -> {
                throw IllegalArgumentException("Invalid AreaMeasureMethod value: $value")
            }
        }
        fun fromStringOrNull(value: String?): AreaMeasureMethod? {
            if (value.isNullOrEmpty()) return null

            return try {
                fromString(value)
            } catch (e: java.lang.IllegalArgumentException) {
                null
            }
        }
    }
}

fun AreaMeasureMethod.asString() = this.value
fun AreaMeasureMethod?.asStringOrNull() = this?.value