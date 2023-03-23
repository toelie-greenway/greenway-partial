package greenway_myanmar.org.features.tag.domain.model

data class TagProduct(
    val id: String,
    val productName: String,
    val distributorName: String,
    val imageUrls: List<String>
)