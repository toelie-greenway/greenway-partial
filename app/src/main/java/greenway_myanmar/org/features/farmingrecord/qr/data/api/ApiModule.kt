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
          .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyIiwianRpIjoiMzA2NGUyNzY5ZDcxZDczODU0YzQwODFmMjc4YzMxZDBlMTM1MjUwNDM4OGRmOGMwMDg5M2JhMjIzYzQxMDQ1ZWZkNTViZDc4YjBlODExOGMiLCJpYXQiOjE2NjE3NDk0NjYuMDIwODkzLCJuYmYiOjE2NjE3NDk0NjYuMDIwOSwiZXhwIjoxNjY0MzQxNDY1LjkzMTE1NSwic3ViIjoiMTA3Iiwic2NvcGVzIjpbXX0.c0zFxO7KCXxNr0Sc5oCRFn7RG9tBNXX_mQi5zm5SPQXVyJ6aAE6PZV2mUIpDSBzceqGCWeCsUuv-S1PlWYLoVnH5ulT91Ik33MoJc-56PPDSSK1Tpmkc6Lw6eH2ppN4XIidVnwxRdWVUsZ5fTp-Hc4aTg5anfUsVJp7vJWemhAwRe3Cjzk-Ktm1IOtohJ4eT7k4JB71a8xIqyCkWiqQISJ6BgAayStK8Mu4qDv5P9wBkA1HO19dptuqVLvanopL4adanuvd4zZOz9wr8s4iiyMeuZneDFMdwtJSmSDvqQ5G0wHpHZXO-VvnG6pwIMFhjiD-NZzSCVOnFBHFxFrZ-gGWhbJckp_X-ioJG9viGLun8boppoWuBZXFYAA3_4s9-EQjV6tODLGonGS0jRfcEAKQmNBNpdun4Xc0HP0kbZocaC7h_2nopVRjRk7NuB37_9hO1BUMM5erpU8ruaYbLYeIOPN2RVlwAyB3FpCcJHkqdEG0k4ar1Pcr-Ax1uiPqrUsjVkSbpIdLVOgRSWeBUFQ4AWDe_hpWWMKNxJIh1EE5upVE7MNXmfjS6m18sFP5B8Nn0-iYYu4Fldatagj7-8ASQuL2FhShZz-O24nmJTzzabLdsvEce8jm3-vlFsbbiSC4yDgNd4-cWVH0docAHt8YQvvPp2xNMFoCGdFEHYvc")
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
