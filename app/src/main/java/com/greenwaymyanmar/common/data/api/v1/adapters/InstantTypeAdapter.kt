package com.greenwaymyanmar.common.data.api.v1.adapters

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant
import java.time.format.DateTimeFormatter

private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

class InstantTypeAdapter : JsonDeserializer<Instant>, JsonSerializer<Instant> {

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): Instant {
    return Instant.parse(json?.asString)
  }

  override fun serialize(
    src: Instant?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(FORMATTER.format(src))
  }
}
