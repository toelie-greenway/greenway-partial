package greenway_myanmar.org.common.data.prefs.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenPreferences(val accessToken: PrefToken, val fcmToken: String) {
  companion object {
    val Empty = TokenPreferences(PrefToken.INVALID, "")
  }
}
