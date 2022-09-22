package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("infos") val userInfo: ApiUserInfo? = null
) {
    data class ApiUserInfo(
        @SerializedName("career") val career: String
    )
}