package greenway_myanmar.org.common.data.prefs

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import greenway_myanmar.org.common.data.prefs.model.PrefToken
import greenway_myanmar.org.common.data.prefs.model.TokenPreferences
import greenway_myanmar.org.common.data.prefs.model.UserPreferences
import greenway_myanmar.org.common.domain.entities.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

interface Preferences {
  suspend fun setAuthState(authState: AuthState)
  fun observeAuthState(): Flow<AuthState>
  fun getAuthState(): AuthState

  suspend fun setToken(token: PrefToken)
  val token: Flow<PrefToken>

  suspend fun logout()
  fun deleteTokenInfo()
}

class DefaultPreferences
constructor(
  private val sharedPreferences: SharedPreferences,
  private val preferenceStore: DataStore<androidx.datastore.preferences.core.Preferences>,
  private val userPreferencesStore: DataStore<UserPreferences>,
  private val tokenPreferencesStore: DataStore<TokenPreferences>,
) : Preferences {

  override suspend fun setAuthState(authState: AuthState) {
    userPreferencesStore.updateData { it.copy(authState = authState) }
  }

  override fun observeAuthState(): Flow<AuthState> {
    return userPreferencesStore.data.map { it.authState }
  }

  override fun getAuthState(): AuthState {
    return runBlocking { observeAuthState().first() }
  }

  override suspend fun setToken(token: PrefToken) {
    tokenPreferencesStore.updateData { it.copy(accessToken = token) }
  }

  override val token: Flow<PrefToken>
    get() = tokenPreferencesStore.data.map { it.accessToken }

  override suspend fun logout() {
    userPreferencesStore.updateData { UserPreferences.Empty }
    tokenPreferencesStore.updateData { TokenPreferences.Empty }
  }

  override fun deleteTokenInfo() {
    runBlocking { tokenPreferencesStore.updateData { it.copy(accessToken = PrefToken.INVALID) } }
  }
}
