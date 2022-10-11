package greenway_myanmar.org.common.data.prefs.model

import com.greenwaymyanmar.common.data.api.v2.model.ApiToken
import kotlinx.serialization.Serializable

@Serializable
data class PrefToken(
  val tokenType: String,
  val expirationTime: Long,
  val accessToken: String,
  val refreshToken: String
) {
  companion object {
    val INVALID = PrefToken("", -1L, "", "")

    fun from(apiToken: ApiToken): PrefToken {
      return PrefToken(
        tokenType = apiToken.tokenType.orEmpty(),
        expirationTime = apiToken.expiresAt,
        accessToken = apiToken.accessToken.orEmpty(),
        refreshToken = apiToken.refreshToken.orEmpty()
      )
    }
  }
}
