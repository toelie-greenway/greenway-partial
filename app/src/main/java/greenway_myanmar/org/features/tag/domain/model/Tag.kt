package greenway_myanmar.org.features.tag.domain.model

data class Tag(
    val id: String,
    val name: String,
    val category: String, //TODO: change category
    val imageUrls: List<String>
)
