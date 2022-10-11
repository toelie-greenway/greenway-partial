package com.greenwaymyanmar.common.data.api.v2.interceptors

import com.google.gson.Gson
import com.greenwaymyanmar.common.data.api.v2.ApiEndpoints
import com.greenwaymyanmar.common.data.api.v2.Protected
import com.greenwaymyanmar.common.data.api.v2.model.ApiToken
import greenway_myanmar.org.BuildConfig
import greenway_myanmar.org.common.data.prefs.Preferences
import greenway_myanmar.org.common.data.prefs.model.PrefToken
import greenway_myanmar.org.common.domain.entities.AuthState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import java.time.Instant
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(private val preferences: Preferences) :
  Interceptor {

  companion object {
    const val UNAUTHORIZED = 401
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request =
      chain
        .request()
        .newBuilder()
        .header("API-KEY", "chXRMV10ATMfeNtfeKyhozSWuMOYhUby")
        .build()

    if (!isProtected(request)) {
      return chain.proceed(request)
    }

    var accessToken = ""
    var tokenExpirationTime = Instant.MIN

    runBlocking {
      preferences.token.firstOrNull()?.let {
        accessToken = it.accessToken
        tokenExpirationTime = Instant.ofEpochSecond(it.expirationTime)
      }
    }

    val interceptedRequest: Request

    if (tokenExpirationTime.isAfter(Instant.now())) {
      // token is still valid, so we can proceed with the request
      interceptedRequest = chain.createAuthenticatedRequest(accessToken)
    } else {
      // Token expired. Gotta refresh it before proceeding with the actual request
      val tokenRefreshResponse = chain.refreshToken()

      interceptedRequest =
        if (tokenRefreshResponse.isSuccessful) {
          val newToken = mapToken(tokenRefreshResponse)

          if (newToken.isValid()) {
            saveNewToken(newToken)
            chain.createAuthenticatedRequest(newToken.accessToken!!)
          } else {
            request
          }
        } else {
          request
        }
    }

    return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
  }

  private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
    val tokenType: String = runBlocking { preferences.token.firstOrNull()?.tokenType.orEmpty() }
    return request()
      .newBuilder()
      .header("Accept", "application/json")
      .header("Content-Type", "application/json")
      .header("Authorization", "$tokenType $token")
      .header("API-KEY", "chXRMV10ATMfeNtfeKyhozSWuMOYhUby")
      .build()
  }

  private fun Interceptor.Chain.refreshToken(): Response {
    val token = runBlocking { preferences.token.firstOrNull() }
    val accessToken = token?.accessToken.orEmpty()
    val refreshToken = token?.refreshToken.orEmpty()
    val tokenType = token?.tokenType.orEmpty()

    val body = FormBody.Builder().add("refresh_token", refreshToken).build()

    val tokenRefresh =
      request()
        .newBuilder()
        .post(body)
        .url(ApiEndpoints.REFRESH_TOKEN_URL)
        .addHeader("Authorization", "$tokenType $accessToken")
        .header("API-KEY", "chXRMV10ATMfeNtfeKyhozSWuMOYhUby")
        .build()

    return proceedDeletingTokenIfUnauthorized(tokenRefresh)
  }

  private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request): Response {
    val response = proceed(request)

    if (response.code == UNAUTHORIZED) {
      if (preferences.getAuthState() == AuthState.LOGGED_IN) {
        runBlocking { preferences.setAuthState(AuthState.LOGGED_OUT) }
      }
      preferences.deleteTokenInfo()
    }

    return response
  }

  private fun isProtected(request: Request): Boolean {
    val protectedTag =
      request.tag(Invocation::class.java)?.method()?.getAnnotation(Protected::class.java)
    return protectedTag != null
  }

  private fun mapToken(tokenRefreshResponse: Response): ApiToken {
    return try {
      val gson = Gson()
      gson.fromJson(tokenRefreshResponse.body!!.string(), ApiToken::class.java)
    } catch (e: Exception) {
      ApiToken.INVALID
    }
  }

  private fun saveNewToken(apiToken: ApiToken) {
    runBlocking { preferences.setToken(PrefToken.from(apiToken)) }
  }
}
