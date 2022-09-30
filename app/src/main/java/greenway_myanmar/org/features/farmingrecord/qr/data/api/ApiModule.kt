package greenway_myanmar.org.features.farmingrecord.qr.data.api

import android.app.Application
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.greenwaymyanmar.api.gson.adapters.InstantTypeAdapter
import com.greenwaymyanmar.api.gson.adapters.OffsetDateTimeTypeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import greenway_myanmar.org.BuildConfig
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Instant
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.SocketFactory

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
      .setDateFormat("dd/MM/yyyy HH:mm:ss")
      .serializeNulls()
      .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeTypeAdapter())
      .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
      .create()
  }

  @Singleton
  @Provides
  fun provideOkHttpClientBuilder(
    app: Application,
    connectionSpec: ConnectionSpec,
    loggingInterceptor: HttpLoggingInterceptor
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
    builder.connectTimeout(1, TimeUnit.MINUTES)
    builder.readTimeout(1, TimeUnit.MINUTES)
    builder.writeTimeout(5, TimeUnit.MINUTES)

    builder.addInterceptor { chain ->
      val newRequest =
        chain.request().newBuilder()
          .header("Accept", "application/json")
          .header("Content-Type", "application/json")
          .header("API-KEY", "chXRMV10ATMfeNtfeKyhozSWuMOYhUby")
          .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyIiwianRpIjoiYjRiODNkNDM2MGFjMDgzYjNhMjQ0ZmYxMjkxMmY0OTI0ZWZiZTk0YmM4MzNiZGIyOTgxZDYzMWY5Yjc5ODA4Yzc0NmVmMmZhMjhiMjgyOTIiLCJpYXQiOjE2NjQzNTM5MTUuODYwNzU3LCJuYmYiOjE2NjQzNTM5MTUuODYxMDExLCJleHAiOjE2NjY5NDU5MTUuNzk3NjY2LCJzdWIiOiIxMDciLCJzY29wZXMiOltdfQ.tLw197u9E-oFmhMeknHGFQrVAO4Sw6R2AmY0bwe1yk42GYInLTuhAnJ8fZPPWCzjKjxW2fASuKQhKmQSwS3UnLoVi8ufAZ7xsPWE6iHVYf1g_Mof5uf8FDWyCALWtUFIGn2KYf8f8rdfRZNbmHgVjX2TlvT_iAxbDD_yys8-Is3xjIlOkajMCgdCKN9q5br46FNk_HCwdpSIvWs0-kznOCu25N7cuzuuDo-y8vaquIWgJvRmf6OotdYgUGkRzZZfdpuldPWjN8-XCMdZwEpXve6uXmb-zKmQfE6295rncdEI0y2NTwJlwa7P7XXm9J5466dXjPUI18QYnRqoupf-9jXJo10-Sx4lUMnG5Yua37S7omO6GcrAzNodthaHuTZsPHM7NEVT2tgC6Xa2Uyo6pMb_jrtqQRe5K9CgsqM2RCPeNWpWGF3q9XJTBohewlo6bvLU2gCqbvJOyqQHVFZyCxe81KoFK9StJKeeqUrNCH2DL8ZgDFW-UnUdjYV8Hly3gkptoQT69R_i8_-tC5QzTE4kJOVXviLNmqDK5-PPArVDJLdiawb9Rg8lJYImR5AoFof4YsKxXgS27QALBJpu9zBqi3Tpb4wW9BOsAz3RaAX9u-Wn2kgVRZ1eWmsmBb1D8mqoGsmP2j3cZUpxSwvEapDBobtcorv21mIQ2osWfxQ")
          .build()
      chain.proceed(newRequest)
    }

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
    gson: Gson
  ): Retrofit.Builder {
    return Retrofit.Builder()
      .baseUrl(url)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
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
    return "https://upgrade.greenwaymyanmar.com/api/v9/".toHttpUrl()
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
  fun provideQrService(retrofit: Retrofit): QrService = retrofit.create(QrService::class.java)

}
