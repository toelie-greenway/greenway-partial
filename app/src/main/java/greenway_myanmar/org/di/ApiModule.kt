package greenway_myanmar.org.di

import android.app.Application
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.greenwaymyanmar.common.data.api.util.ApiResponseCallAdapterFactory
import com.greenwaymyanmar.common.data.api.util.LiveDataCallAdapterFactory
import com.greenwaymyanmar.common.data.api.v1.gson.AnnotationExclusionStrategy
import com.greenwaymyanmar.common.data.api.v1.services.GreenWayWebservice
import com.greenwaymyanmar.common.data.api.v2.interceptors.AuthenticationInterceptor
import com.greenwaymyanmar.common.data.api.v2.interceptors.NetworkStatusInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.BuildConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .setExclusionStrategies(AnnotationExclusionStrategy())
      .setDateFormat("dd/MM/yyyy HH:mm:ss")
      .serializeNulls()
      .create()
  }

  @Provides
  @Singleton
  fun providesNetworkJson(): Json = Json {
    ignoreUnknownKeys = true
  }

  @Singleton
  @Provides
  fun provideOkHttpClientBuilder(
    app: Application,
    connectionSpec: ConnectionSpec,
    networkStatusInterceptor: NetworkStatusInterceptor,
    loggingInterceptor: HttpLoggingInterceptor,
    authenticationInterceptor: AuthenticationInterceptor
  ): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
    builder.addInterceptor(
      ChuckerInterceptor.Builder(app)
        .collector(ChuckerCollector(app))
        .maxContentLength(250000L)
        .redactHeaders(emptySet())
        .alwaysReadResponseBody(false)
        .build(),
    )
    builder.connectTimeout(15, TimeUnit.SECONDS)
    builder.readTimeout(15, TimeUnit.SECONDS)
    builder.writeTimeout(15, TimeUnit.SECONDS)
    builder.addInterceptor(networkStatusInterceptor)
    builder.addInterceptor(authenticationInterceptor)
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(loggingInterceptor)
    }

    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
      builder.connectionSpecs(listOf(connectionSpec))
    }
    return builder
  }

  @Singleton
  @Provides
  fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient {
    return builder.build()
  }

  @Provides
  @Singleton
  fun provideRetrofitBuilder(
    url: HttpUrl,
    okHttpClient: OkHttpClient,
    gson: Gson,
    networkJson: Json,
  ): Retrofit.Builder {
    return Retrofit.Builder()
      .baseUrl(url)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(LiveDataCallAdapterFactory())
      .addCallAdapterFactory(ApiResponseCallAdapterFactory())
      .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
      .client(okHttpClient)
  }

  @Provides
  @Singleton
  fun provideRetrofit(builder: Retrofit.Builder): Retrofit {
    return builder.build()
  }

  @Singleton
  @Provides
  fun provideHttpUrl(): HttpUrl {
    return "http://10.0.2.2:3003/api/v9/".toHttpUrl()
  }

  @Provides
  @Singleton
  fun provideConnectionSpec(): ConnectionSpec {
    return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_2)
      .cipherSuites(CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
      .build()
  }
}

@InstallIn(SingletonComponent::class)
@Module
object GreenWayApiServiceModule {

  @Provides
  @Singleton
  fun provideGreenWayService(retrofit: Retrofit): GreenWayWebservice =
    retrofit.create(GreenWayWebservice::class.java)

}
