package greenway_myanmar.org.common.data.prefs.model

import greenway_myanmar.org.common.domain.entities.AuthState
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(val authState: AuthState = AuthState.LOGGED_OUT) {
  companion object {
    val Empty = UserPreferences()
  }
}
