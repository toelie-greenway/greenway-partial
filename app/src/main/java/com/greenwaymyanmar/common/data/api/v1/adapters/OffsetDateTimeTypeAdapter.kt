package com.greenwaymyanmar.common.data.api.v1.adapters

import com.google.gson.*
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

class OffsetDateTimeTypeAdapter : JsonDeserializer<OffsetDateTime>, JsonSerializer<OffsetDateTime> {

  override fun deserialize(
    json: JsonElement?,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): OffsetDateTime {
    return OffsetDateTime.parse(json?.asString)
  }

  override fun serialize(
    src: OffsetDateTime?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(FORMATTER.format(src))
  }
}
