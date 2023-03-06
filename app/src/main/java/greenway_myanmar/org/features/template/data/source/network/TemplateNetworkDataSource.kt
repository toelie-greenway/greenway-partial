package greenway_myanmar.org.features.template.data.source.network

import greenway_myanmar.org.features.template.data.source.network.model.NetworkImage
import okhttp3.RequestBody

/**
 * Interface representing network calls to the GreenWay backend
 */
interface TemplateNetworkDataSource {

    suspend fun postImage(
        params: Map<String, RequestBody?>
    ): NetworkImage

}