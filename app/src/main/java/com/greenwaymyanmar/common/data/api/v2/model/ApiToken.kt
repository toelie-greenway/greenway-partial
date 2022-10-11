package com.greenwaymyanmar.common.data.api.v2.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class ApiToken(
  @SerializedName("token_type") val tokenType: String?,
  @SerializedName("expires_in") val expiresInSeconds: Long?,
  @SerializedName("access_token") val accessToken: String?,
  @SerializedName("refresh_token") val refreshToken: String?
) {

  companion object {
    val INVALID = ApiToken("", -1, "", "")
  }

  private val requestedAt: Instant
    get() = Instant.now()

  val expiresAt: Long
    get() {
      if (expiresInSeconds == null) return 0L

      return requestedAt.plusSeconds(expiresInSeconds.toLong()).epochSecond
    }

  fun isValid(): Boolean {
    return tokenType != null &&
      tokenType.isNotEmpty() &&
      expiresInSeconds != null &&
      expiresInSeconds >= 0 &&
      accessToken != null &&
      accessToken.isNotEmpty() &&
      refreshToken != null &&
      refreshToken.isNotEmpty()
  }
}
