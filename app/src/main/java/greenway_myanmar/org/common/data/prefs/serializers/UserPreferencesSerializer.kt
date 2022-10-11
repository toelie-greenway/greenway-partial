package greenway_myanmar.org.common.data.prefs.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import greenway_myanmar.org.common.data.prefs.model.UserPreferences
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object UserPreferencesSerializer : Serializer<UserPreferences> {
  override val defaultValue = UserPreferences()

  override suspend fun readFrom(input: InputStream): UserPreferences {
    try {
      return Json.decodeFromString(UserPreferences.serializer(), input.readBytes().decodeToString())
    } catch (serialization: SerializationException) {
      throw CorruptionException("Unable to read UserPrefs", serialization)
    }
  }

  override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
    output.write(Json.encodeToString(UserPreferences.serializer(), t).encodeToByteArray())
  }
}
