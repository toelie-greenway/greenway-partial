package greenway_myanmar.org.features.template.data.source.network.model

import kotlinx.serialization.Serializable

/**
 * Wrapper for data provided from the API
 */
@Serializable
data class ApiDataWrapper<T>(
    val data: T
)
