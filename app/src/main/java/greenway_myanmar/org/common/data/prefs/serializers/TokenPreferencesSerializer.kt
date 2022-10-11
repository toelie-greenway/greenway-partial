package greenway_myanmar.org.common.data.prefs.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import greenway_myanmar.org.common.data.prefs.model.TokenPreferences
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object TokenPreferencesSerializer : Serializer<TokenPreferences> {
  override val defaultValue = TokenPreferences.Empty

  override suspend fun readFrom(input: InputStream): TokenPreferences {
    try {
      return Json.decodeFromString(
        TokenPreferences.serializer(),
        input.readBytes().decodeToString()
      )
    } catch (serialization: SerializationException) {
      throw CorruptionException("Unable to read UserPrefs", serialization)
    }
  }

  override suspend fun writeTo(t: TokenPreferences, output: OutputStream) {
    output.write(Json.encodeToString(TokenPreferences.serializer(), t).encodeToByteArray())
  }
}
