package greenway_myanmar.org.features.farmingrecord.qr.domain.model

enum class QrOrderStatus(val value: String) {
    Ordered("ordered"),
    Confirmed("confirmed");

    companion object {
        fun fromString(value: String): QrOrderStatus {
            return when (value) {
                Ordered.value -> Ordered
                Confirmed.value -> Confirmed
                else -> Ordered
            }
        }
    }
}